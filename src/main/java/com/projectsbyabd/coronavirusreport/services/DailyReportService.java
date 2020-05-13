package com.projectsbyabd.coronavirusreport.services;

import com.projectsbyabd.coronavirusreport.models.DailyReportEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DailyReportService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<DailyReportEntry> dailyReportEntries = new ArrayList<>();
    private List<DailyReportEntry> aggregatedDailyReportEntries = new ArrayList<>();
    private String lastUpdatedTime = "00:00 (UTC)";

    @Value("${useTestData}")
    private boolean useTestData;
    @Value("${testDataPath}")
    private String testDataPath;
    @Value("${dailyReportUrlPrefix}")
    private String dailyReportUrlPrefix;
    @Value("${pastDaysToCheck}")
    private Integer pastDaysToCheck;
    @Value("${retriesPerDay}")
    private Integer retriesPerDay;
    @Value("${connectTimeout}")
    private Integer connectTimeout;
    @Value("${readTimeout}")
    private Integer readTimeout;

    @PostConstruct
    @Scheduled(cron = "${scheduledCron}", zone = "${scheduledZone}")
    public void getDailyReportData() {
        String data;

        if (useTestData) {
            data = getTestData();
        } else {
            data = getLatestData();
        }

        parseData(data);
    }

    private String getTestData() {
        File file = new File(testDataPath);
        StringBuilder stringBuilder;

        try (Scanner scanner = new Scanner(file)) {
            stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append(System.getProperty("line.separator"));
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            logger.error("Error finding file - Error Message: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error reading file - Error Message: {}", e.getMessage());
            return null;
        }
    }

    private String getLatestData() {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (int i = 0; i < pastDaysToCheck; i++) {
            for (int j = 0; j < retriesPerDay; j++) {
                Date date = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(i));
                String dateString = dateFormat.format(date);
                String dateUrl = String.format("%s%s.csv", dailyReportUrlPrefix, dateString);

                try {
                    ResponseEntity<String> response = restTemplate.getForEntity(dateUrl, String.class);
                    if (response.getStatusCodeValue() == 200) {
                        lastUpdatedTime = String.format("%s at %s (UTC)", dateFormat.format(new Date()), timeFormat.format(new Date()));
                        logger.info("Successfully fetched data for {} (UTC)", dateString);
                        logger.info("Last updated on {}", lastUpdatedTime);
                        return response.getBody();
                    }
                } catch (RestClientResponseException e) {
                    logger.error("HTTP error fetching data for {} (UTC) - Error Code: {}", dateString, e.getRawStatusCode());
                } catch (ResourceAccessException e) {
                    logger.error("Timeout error fetching data for {} (UTC) - Error Message: {}", dateString, e.getMessage());
                } catch (Exception e) {
                    logger.error("Error fetching data for {} (UTC) - Error Message: {}", dateString, e.getMessage());
                }
            }
        }

        return null;
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return clientHttpRequestFactory;
    }

    private void parseData(String rawData) {
        List<DailyReportEntry> newReportEntries = new ArrayList<>();
        StringReader reader = new StringReader(rawData);

        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String regionKey = record.get("Combined_Key");
                String country = record.get("Country_Region");
                Integer confirmed = Integer.parseInt(record.get("Confirmed"));
                Integer deaths = Integer.parseInt(record.get("Deaths"));
                Integer recovered = Integer.parseInt(record.get("Recovered"));
                Integer active = Integer.parseInt(record.get("Active"));
                DailyReportEntry dailyReportEntry = new DailyReportEntry(regionKey, country, confirmed, deaths, recovered, active);
                newReportEntries.add(dailyReportEntry);
            }

            dailyReportEntries = newReportEntries;
            setAggregatedDailyReportEntries();
        } catch (IOException e) {
            logger.error("Error parsing data - Error Message: {}", e.getMessage());
        }
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public List<DailyReportEntry> getDailyReportEntries() {
        return dailyReportEntries;
    }

    public void setAggregatedDailyReportEntries() {
        aggregatedDailyReportEntries = dailyReportEntries.stream()
                .collect(Collectors.groupingBy(DailyReportEntry::getCountry))
                .values().stream()
                .map(reportEntries -> reportEntries.stream()
                        .reduce((e1, e2) -> new DailyReportEntry(e1.getCountry(), e1.getCountry(), e1.getConfirmed() + e2.getConfirmed(), e1.getDeaths() + e2.getDeaths(), e1.getRecovered() + e2.getRecovered(), e1.getActive() + e2.getActive())))
                .map(Optional::get)
                .sorted(Comparator.comparing(DailyReportEntry::getRegionKey))
                .collect(Collectors.toList());
    }

    public List<DailyReportEntry> getAggregatedDailyReportEntries() {
        return aggregatedDailyReportEntries;
    }

    public List<DailyReportEntry> getTopConfirmedDailyReportEntries(Integer limit) {
        return aggregatedDailyReportEntries.stream()
                .sorted(Comparator.comparingInt(DailyReportEntry::getConfirmed).reversed())
                .limit(limit).collect(Collectors.toList());
    }

    public List<DailyReportEntry> getTopDeathsDailyReportEntries(Integer limit) {
        return aggregatedDailyReportEntries.stream()
                .sorted(Comparator.comparingInt(DailyReportEntry::getDeaths).reversed())
                .limit(limit).collect(Collectors.toList());
    }

    public List<DailyReportEntry> getTopRecoveredDailyReportEntries(Integer limit) {
        return aggregatedDailyReportEntries.stream()
                .sorted(Comparator.comparingInt(DailyReportEntry::getRecovered).reversed())
                .limit(limit).collect(Collectors.toList());
    }

    public List<DailyReportEntry> getTopActiveDailyReportEntries(Integer limit) {
        return aggregatedDailyReportEntries.stream()
                .sorted(Comparator.comparingInt(DailyReportEntry::getActive).reversed())
                .limit(limit).collect(Collectors.toList());
    }

    public DailyReportEntry getAggregatedDailyReportEntry(String country) {
        return aggregatedDailyReportEntries.stream()
                .filter(dailyReportEntry -> country.equals(dailyReportEntry.getCountry()))
                .findAny().orElse(null);
    }

    public DailyReportEntry getDailyReportEntry(String regionKey) {
        return dailyReportEntries.stream()
                .filter(dailyReportEntry -> regionKey.equals(dailyReportEntry.getRegionKey()))
                .findAny().orElse(null);
    }

    public List<String> getAllRegionKeys() {
        return dailyReportEntries.stream()
                .map(DailyReportEntry::getRegionKey).collect(Collectors.toList());
    }

    public List<String> getAllCountries() {
        return aggregatedDailyReportEntries.stream()
                .map(DailyReportEntry::getCountry).collect(Collectors.toList());
    }

    public Integer getTotalConfirmed() {
        return dailyReportEntries.stream()
                .mapToInt(DailyReportEntry::getConfirmed).sum();
    }

    public Integer getTotalDeaths() {
        return dailyReportEntries.stream()
                .mapToInt(DailyReportEntry::getDeaths).sum();
    }

    public Integer getTotalRecovered() {
        return dailyReportEntries.stream()
                .mapToInt(DailyReportEntry::getRecovered).sum();
    }

    public Integer getTotalActive() {
        return dailyReportEntries.stream()
                .mapToInt(DailyReportEntry::getActive).sum();
    }

    public Integer getSpecificStat() {
        return dailyReportEntries.stream()
                .mapToInt(DailyReportEntry::getActive).sum();
    }

    public Integer getSpecificStat(String regionKey, String country, String attribute) {

        DailyReportEntry specificEntry = dailyReportEntries.stream()
                .filter(dailyReportEntry -> regionKey.equals(dailyReportEntry.getRegionKey()) && country.equals(dailyReportEntry.getCountry()))
                .findAny().orElse(null);

        if (specificEntry == null) {
            return null;
        }

        switch(attribute) {
            case "confirmed":
                return specificEntry.getConfirmed();
            case "deaths":
                return specificEntry.getDeaths();
            case "recovered":
                return specificEntry.getRecovered();
            case "active":
                return specificEntry.getActive();
            default:
                return null;
        }
    }
}
