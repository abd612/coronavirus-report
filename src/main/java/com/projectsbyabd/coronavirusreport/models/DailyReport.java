package com.projectsbyabd.coronavirusreport.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class DailyReport {

    @Id
    private String dateId;
    private Date date;
    private List<DailyReportEntry> dailyReportEntries;

    public DailyReport() {
    }

    public DailyReport(String dateId, Date date, List<DailyReportEntry> dailyReportEntries) {
        this.dateId = dateId;
        this.date = date;
        this.dailyReportEntries = dailyReportEntries;
    }

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<DailyReportEntry> getDailyReportEntries() {
        return dailyReportEntries;
    }

    public void setDailyReportEntries(List<DailyReportEntry> dailyReportEntries) {
        this.dailyReportEntries = dailyReportEntries;
    }
}
