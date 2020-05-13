package com.projectsbyabd.coronavirusreport.models;

import org.springframework.stereotype.Component;

@Component
public class DailyReportEntry {

    private String regionKey;
    private String country;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    private Integer active;

    public DailyReportEntry() {
    }

    public DailyReportEntry(String regionKey, String country, Integer confirmed, Integer deaths, Integer recovered, Integer active) {
        this.regionKey = regionKey;
        this.country = country;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
    }

    public String getRegionKey() {
        return regionKey;
    }

    public void setRegionKey(String regionKey) {
        this.regionKey = regionKey;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "DailyReportEntry{" +
                "regionKey='" + regionKey + '\'' +
                ", country='" + country + '\'' +
                ", confirmed=" + confirmed +
                ", deaths=" + deaths +
                ", recovered=" + recovered +
                ", active=" + active +
                '}';
    }
}
