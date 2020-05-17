package com.projectsbyabd.coronavirusreport.services;

import com.projectsbyabd.coronavirusreport.models.DailyReportEntry;
import com.projectsbyabd.coronavirusreport.repositories.PostgresRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostgresService {

    private static final Logger logger = LoggerFactory.getLogger(PostgresService.class);

    @Autowired
    private PostgresRepository postgresRepository;

    public List<DailyReportEntry> getAllEntries() {
        Spliterator<DailyReportEntry> spliterator = postgresRepository.findAll().spliterator();
        return StreamSupport.stream(spliterator, false).collect(Collectors.toList());
    }

    public DailyReportEntry getEntryByRegionKey(String regionKey) {
        return postgresRepository.findByRegionKey(regionKey);
    }

    public DailyReportEntry insertEntry(DailyReportEntry dailyReportEntry) {
        return postgresRepository.save(dailyReportEntry);
    }

    public DailyReportEntry updateEntry(DailyReportEntry dailyReportEntry) {
        return postgresRepository.save(dailyReportEntry);
    }

    @Async
    public void updateAllEntries(List<DailyReportEntry> dailyReportEntries) {
        try {
            logger.info("Purging old data in Postgres");
            postgresRepository.deleteAll();
            logger.info("Adding new data in Postgres");
            postgresRepository.saveAll(dailyReportEntries);
            logger.info("Data updated in Postgres");
        } catch (Exception e) {
            logger.error("Error updating data in Postgres - Error Message: {}", e.getMessage());
        }
    }

    public void deleteEntry(DailyReportEntry dailyReportEntry) {
        postgresRepository.delete(dailyReportEntry);
    }

    public void deleteEntryByRegionKey(String regionKey) {
        postgresRepository.deleteByRegionKey(regionKey);
    }
}
