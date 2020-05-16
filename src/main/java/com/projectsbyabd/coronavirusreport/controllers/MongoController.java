package com.projectsbyabd.coronavirusreport.controllers;

import com.projectsbyabd.coronavirusreport.models.DailyReport;
import com.projectsbyabd.coronavirusreport.services.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private MongoService mongoService;

    @GetMapping("/getAllReports")
    public List<DailyReport> getAllReports() {
        return mongoService.getAllReports();
    }

    @GetMapping("/getReportByDateId")
    public DailyReport getReportByDateId(@RequestParam String dateId) {
        return mongoService.getReportByDateId(dateId);
    }

    @GetMapping("/getLatestReport")
    public DailyReport getLatestReport() {
        return mongoService.getLatestReport();
    }

    @PostMapping("/insertReport")
    public DailyReport insertReport(@RequestBody DailyReport dailyReport) {
        return mongoService.insertReport(dailyReport);
    }

    @PutMapping("/updateReport")
    public DailyReport updateReport(@RequestBody DailyReport dailyReport) {
        return mongoService.updateReport(dailyReport);
    }

    @DeleteMapping("/deleteReport")
    public void deleteReport(DailyReport dailyReport) {
        mongoService.deleteReport(dailyReport);
    }
}
