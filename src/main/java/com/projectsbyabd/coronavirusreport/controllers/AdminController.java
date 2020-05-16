package com.projectsbyabd.coronavirusreport.controllers;

import com.projectsbyabd.coronavirusreport.services.DailyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DailyReportService dailyReportService;

    @GetMapping("/loadDataFromMongo")
    public String loadDataFromMongo() {
        dailyReportService.loadDataFromMongo();
        return "loaded";
    }

    @GetMapping("/loadDataFromPostgres")
    public String loadDataFromPostgres() {
        dailyReportService.loadDataFromPostgres();
        return "loaded";
    }

    @GetMapping("/ping")
    public String ping() {
        return "success";
    }

    @GetMapping("/updateDataFromSource")
    public String updateDataFromSource() {
        dailyReportService.updateDataFromSource();
        return "updated";
    }

    @GetMapping("/updateDataInMongo")
    public String updateDataInMongo() {
        dailyReportService.updateDataInMongo();
        return "updated";
    }

    @GetMapping("/updateDataInPostgres")
    public String updateDataInPostgres() {
        dailyReportService.updateDataInPostgres();
        return "initiated";
    }

    @GetMapping("/triggerScheduledFunction")
    public String triggerScheduledFunction() {
        dailyReportService.scheduledFunction();
        return "triggered";
    }

    @GetMapping("/triggerStartupFunction")
    public String triggerStartupFunction() {
        dailyReportService.startupFunction();
        return "triggered";
    }
}
