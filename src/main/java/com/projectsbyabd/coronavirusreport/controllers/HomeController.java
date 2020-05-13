package com.projectsbyabd.coronavirusreport.controllers;

import com.projectsbyabd.coronavirusreport.services.DailyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private DailyReportService dailyReportService;
    @Value("${dailyUpdateTime}")
    private String dailyUpdateTime;
    @Value("${dataSourceLink}")
    private String dataSourceLink;
    @Value("${creditLink}")
    private String creditLink;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("dailyUpdateTime", dailyUpdateTime);
        model.addAttribute("lastUpdatedTime", dailyReportService.getLastUpdatedTime());
        model.addAttribute("dataSourceLink", dataSourceLink);
        model.addAttribute("creditLink", creditLink);
        model.addAttribute("topConfirmedDailyReportEntries", dailyReportService.getTopConfirmedDailyReportEntries(10));
        model.addAttribute("totalConfirmed", dailyReportService.getTotalConfirmed());
        model.addAttribute("totalDeaths", dailyReportService.getTotalDeaths());
        model.addAttribute("totalRecovered", dailyReportService.getTotalRecovered());
        model.addAttribute("totalActive", dailyReportService.getTotalActive());

        return "home";
    }

    @GetMapping("/country-wise-stats")
    public String countryWiseStats(Model model) {
        model.addAttribute("aggregatedDailyReportEntries", dailyReportService.getAggregatedDailyReportEntries());

        return "country-wise-stats";
    }

    @GetMapping("/region-wise-stats")
    public String regionWiseStats(Model model) {
        model.addAttribute("dailyReportEntries", dailyReportService.getDailyReportEntries());

        return "region-wise-stats";
    }

    @GetMapping("/about-me")
    public String aboutMe(Model model) {
        model.addAttribute("creditLink", creditLink);
        return "about-me";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
