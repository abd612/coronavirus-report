package com.projectsbyabd.coronavirusreport.controllers;

import com.projectsbyabd.coronavirusreport.models.DailyReportEntry;
import com.projectsbyabd.coronavirusreport.services.DailyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private DailyReportService dailyReportService;

    @GetMapping("/getAggregatedLatestReport")
    public List<DailyReportEntry> getAggregatedLatestReport() {
        return dailyReportService.getAggregatedDailyReportEntries();
    }

    @GetMapping("/getCompleteLatestReport")
    public List<DailyReportEntry> getCompleteLatestReport() {
        return dailyReportService.getDailyReportEntries();
    }

    @GetMapping("/getLatestReportByCountry")
    public DailyReportEntry getLatestReportByCountry(@RequestParam String country) {
        return dailyReportService.getAggregatedDailyReportEntry(country);
    }

    @GetMapping("/getLatestReportByRegion")
    public DailyReportEntry getLatestReportByRegion(@RequestParam String region) {
        return dailyReportService.getDailyReportEntry(region);
    }

    @GetMapping("/getMostConfirmed")
    public List<DailyReportEntry> getMostConfirmed(@RequestParam Integer numberOfResults) {
        return dailyReportService.getTopConfirmedDailyReportEntries(numberOfResults);
    }

    @GetMapping("/getMostDeaths")
    public List<DailyReportEntry> getMostDeaths(@RequestParam Integer numberOfResults) {
        return dailyReportService.getTopDeathsDailyReportEntries(numberOfResults);
    }

    @GetMapping("/getMostRecovered")
    public List<DailyReportEntry> getMostRecovered(@RequestParam Integer numberOfResults) {
        return dailyReportService.getTopRecoveredDailyReportEntries(numberOfResults);
    }

    @GetMapping("/getMostActive")
    public List<DailyReportEntry> getMostActive(@RequestParam Integer numberOfResults) {
        return dailyReportService.getTopActiveDailyReportEntries(numberOfResults);
    }

    @GetMapping("/getSpecificStat")
    public Integer getSpecificStat(@RequestParam String regionKey, @RequestParam String country, @RequestParam String attribute) {
        return dailyReportService.getSpecificStat(regionKey, country, attribute);
    }

    @GetMapping("/getTotalConfirmed")
    public Integer getTotalConfirmed() {
        return dailyReportService.getTotalConfirmed();
    }

    @GetMapping("/getTotalDeaths")
    public Integer getTotalDeaths() {
        return dailyReportService.getTotalDeaths();
    }

    @GetMapping("/getTotalRecovered")
    public Integer getTotalRecovered() {
        return dailyReportService.getTotalRecovered();
    }

    @GetMapping("/getTotalActive")
    public Integer getTotalActive() {
        return dailyReportService.getTotalActive();
    }

    @GetMapping("/listAllCountries")
    public List<String> listAllCountries() {
        return dailyReportService.getAllCountries();
    }

    @GetMapping("/listAllRegions")
    public List<String> listAllRegions() {
        return dailyReportService.getAllRegionKeys();
    }

    @GetMapping("/ping")
    public String ping() {
        return "{\"success\": true}";
    }
}
