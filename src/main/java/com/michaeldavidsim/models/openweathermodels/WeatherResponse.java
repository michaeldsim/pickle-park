package com.michaeldavidsim.models.openweathermodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private double lat;
    private double lon;
    private String timezone;
    
    @JsonProperty("timezone_offset")
    private int timezoneOffset;

    private Current current;
    private List<Minutely> minutely;
    private List<Hourly> hourly;
    private List<Daily> daily;
    private List<Alert> alerts;

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public int getTimezoneOffset() { return timezoneOffset; }
    public void setTimezoneOffset(int timezoneOffset) { this.timezoneOffset = timezoneOffset; }

    public Current getCurrent() { return current; }
    public void setCurrent(Current current) { this.current = current; }

    public List<Minutely> getMinutely() { return minutely; }
    public void setMinutely(List<Minutely> minutely) { this.minutely = minutely; }

    public List<Hourly> getHourly() { return hourly; }
    public void setHourly(List<Hourly> hourly) { this.hourly = hourly; }

    public List<Daily> getDaily() { return daily; }
    public void setDaily(List<Daily> daily) { this.daily = daily; }

    public List<Alert> getAlerts() { return alerts; }
    public void setAlerts(List<Alert> alerts) { this.alerts = alerts; }
}
