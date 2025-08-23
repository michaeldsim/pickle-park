package com.michaeldavidsim.utils;

import java.io.IOException;
import java.time.LocalDate;

import com.michaeldavidsim.models.openweathermodels.WeatherResponse;

public class WeatherService {
    private final WeatherFetcher fetcher;
    private final WeatherCache<WeatherResponse> cache = new WeatherCache<>();

    public WeatherService(WeatherFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public WeatherResponse getWeather(String lat, String lon) {
        LocalDate day = LocalDate.now();
        return cache.getOrLoad(day, () -> {
            try {
                return fetcher.fetch(lat, lon);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Weather fetch failed", e);
            }
        });
    }
}
