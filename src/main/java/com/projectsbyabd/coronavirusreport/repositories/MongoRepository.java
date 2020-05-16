package com.projectsbyabd.coronavirusreport.repositories;

import com.mongodb.client.result.DeleteResult;
import com.projectsbyabd.coronavirusreport.models.DailyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoRepository {

    private static final String MONGO_COLLECTION = "daily-reports";

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<DailyReport> getAllReports() {
        return mongoTemplate.findAll(DailyReport.class, MONGO_COLLECTION);
    }

    public DailyReport getReportByDateId(String dateId) {
        return mongoTemplate.findById(dateId, DailyReport.class, MONGO_COLLECTION);
    }

    public DailyReport getLatestReport() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "dateId"));
        return mongoTemplate.findOne(query, DailyReport.class, MONGO_COLLECTION);
    }

    public DailyReport insertReport(DailyReport dailyReport) {
        return mongoTemplate.insert(dailyReport, MONGO_COLLECTION);
    }

    public DailyReport updateReport(DailyReport dailyReport) {
        return mongoTemplate.save(dailyReport, MONGO_COLLECTION);
    }

    public DeleteResult deleteReport(DailyReport dailyReport) {
        return mongoTemplate.remove(dailyReport, MONGO_COLLECTION);
    }
}
