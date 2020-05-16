package com.projectsbyabd.coronavirusreport.controllers;

import com.projectsbyabd.coronavirusreport.models.DailyReportEntry;
import com.projectsbyabd.coronavirusreport.services.PostgresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/postgres")
public class PostgresController {

    @Autowired
    private PostgresService postgresService;

    @GetMapping("/getAllEntries")
    public List<DailyReportEntry> getAllEntries() {
        return postgresService.getAllEntries();
    }

    @GetMapping("/getEntryByRegionKey")
    public DailyReportEntry getEntryByRegionKey(@RequestParam String regionKey) {
        return postgresService.getEntryByRegionKey(regionKey);
    }

    @PostMapping("/insertEntry")
    public DailyReportEntry insertEntry(@RequestBody DailyReportEntry dailyReportEntry) {
        return postgresService.insertEntry(dailyReportEntry);
    }

    @PutMapping("/updateEntry")
    public DailyReportEntry updateEntry(@RequestBody DailyReportEntry dailyReportEntry) {
        return postgresService.updateEntry(dailyReportEntry);
    }

    @DeleteMapping("/deleteEntry")
    public void deleteEntry(DailyReportEntry dailyReportEntry) {
        postgresService.deleteEntry(dailyReportEntry);
    }

    @DeleteMapping("/deleteEntryByRegionKey")
    public void deleteEntryByRegionKey(@RequestParam String regionKey) {
        postgresService.deleteEntryByRegionKey(regionKey);
    }
}
