package com.projectsbyabd.coronavirusreport.services;

import com.mongodb.client.result.DeleteResult;
import com.projectsbyabd.coronavirusreport.models.DailyReport;
import com.projectsbyabd.coronavirusreport.repositories.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoService {
    
    @Autowired
    private MongoRepository mongoRepository;

    public List<DailyReport> getAllReports() {
        return mongoRepository.getAllReports();
    }

    public DailyReport getReportByDateId(String dateId) {
        return mongoRepository.getReportByDateId(dateId);
    }

    public DailyReport getLatestReport() {
        return mongoRepository.getLatestReport();
    }

    public DailyReport insertReport(DailyReport dailyReport) {
        return mongoRepository.insertReport(dailyReport);
    }

    public DailyReport updateReport(DailyReport dailyReport) {
        return mongoRepository.updateReport(dailyReport);
    }

    public DeleteResult deleteReport(DailyReport dailyReport) {
        return mongoRepository.deleteReport(dailyReport);
    }
}
