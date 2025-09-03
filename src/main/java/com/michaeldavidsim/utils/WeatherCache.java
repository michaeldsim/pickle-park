package com.michaeldavidsim.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

public class WeatherCache<T> {
    private static final long TTL_MINUTES = 10;
    private T data;
    private Instant fetchedAt;

    public T getOrLoad(Supplier<T> loader) {
        if (data == null || isExpired()) {
            data = loader.get();
            fetchedAt = Instant.now();
        }
        return data;
    }

    public Optional<T> getCached() {
        if (data == null || isExpired()) {
            return Optional.empty();
        }
        return Optional.of(data);
    }

    private boolean isExpired() {
        return fetchedAt == null || Duration.between(fetchedAt, Instant.now()).toMinutes() >= TTL_MINUTES;
    }
}