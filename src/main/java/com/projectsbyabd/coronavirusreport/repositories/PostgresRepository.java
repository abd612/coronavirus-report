package com.projectsbyabd.coronavirusreport.repositories;

import com.projectsbyabd.coronavirusreport.models.DailyReportEntry;
import org.springframework.data.repository.CrudRepository;

public interface PostgresRepository extends CrudRepository<DailyReportEntry, String> {

    DailyReportEntry findByRegionKey(String regionKey);

    void deleteByRegionKey(String regionKey);
}
