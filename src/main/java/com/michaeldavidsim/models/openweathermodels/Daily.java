package com.michaeldavidsim.models.openweathermodels;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Daily {
    private long dt;
    private long sunrise;
    private long sunset;
    private long moonrise;
    private long moonset;
    private double moon_phase;
    private String summary;

    public long getDt() {
        return dt;
    }
    public void setDt(long dt) {
        this.dt = dt;
    }
    public long getSunrise() {
        return sunrise;
    }
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }
    public long getSunset() {
        return sunset;
    }
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
    public long getMoonrise() {
        return moonrise;
    }
    public void setMoonrise(long moonrise) {
        this.moonrise = moonrise;
    }
    public long getMoonset() {
        return moonset;
    }
    public void setMoonset(long moonset) {
        this.moonset = moonset;
    }
    public double getMoon_phase() {
        return moon_phase;
    }
    public void setMoon_phase(double moon_phase) {
        this.moon_phase = moon_phase;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public Temp getTemp() {
        return temp;
    }
    public void setTemp(Temp temp) {
        this.temp = temp;
    }
    public FeelsLike getFeels_like() {
        return feels_like;
    }
    public void setFeels_like(FeelsLike feels_like) {
        this.feels_like = feels_like;
    }
    public int getPressure() {
        return pressure;
    }
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public double getDew_point() {
        return dew_point;
    }
    public void setDew_point(double dew_point) {
        this.dew_point = dew_point;
    }
    public double getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }
    public int getWind_deg() {
        return wind_deg;
    }
    public void setWind_deg(int wind_deg) {
        this.wind_deg = wind_deg;
    }
    public double getWind_gust() {
        return wind_gust;
    }
    public void setWind_gust(double wind_gust) {
        this.wind_gust = wind_gust;
    }
    public List<Weather> getWeather() {
        return weather;
    }
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
    public int getClouds() {
        return clouds;
    }
    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
    public double getPop() {
        return pop;
    }
    public void setPop(double pop) {
        this.pop = pop;
    }
    public double getRain() {
        return rain;
    }
    public void setRain(double rain) {
        this.rain = rain;
    }
    public double getUvi() {
        return uvi;
    }
    public void setUvi(double uvi) {
        this.uvi = uvi;
    }
    private Temp temp;
    private FeelsLike feels_like;

    private int pressure;
    private int humidity;
    private double dew_point;
    private double wind_speed;
    private int wind_deg;
    private double wind_gust;
    private List<Weather> weather;
    private int clouds;
    private double pop;
    private double rain;
    private double uvi;

}
