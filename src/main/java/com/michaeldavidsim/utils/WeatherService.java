package com.michaeldavidsim.utils;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.michaeldavidsim.models.openweathermodels.WeatherResponse;

public class WeatherService {
    private static final long TTL_MINUTES = 10;
    private final WeatherFetcher fetcher;
    private final Map<String, CachedWeatherResponse> cache = new ConcurrentHashMap<>();

    private static class CachedWeatherResponse {
        final WeatherResponse response;
        final Instant fetchedAt;

        CachedWeatherResponse(WeatherResponse response) {
            this.response = response;
            this.fetchedAt = Instant.now();
        }

        boolean isExpired() {
            return Duration.between(fetchedAt, Instant.now()).toMinutes() >= TTL_MINUTES;
        }
    }

    public WeatherService(WeatherFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public WeatherResponse getWeather(String lat, String lon) {
        String key = lat + "," + lon;
        CachedWeatherResponse cached = cache.get(key);

        if (cached != null && !cached.isExpired()) {
            return cached.response;
        }

        try {
            WeatherResponse response = fetcher.fetch(lat, lon);
            cache.put(key, new CachedWeatherResponse(response));
            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Weather fetch failed", e);
        }
    }
}
