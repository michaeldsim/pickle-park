package com.michaeldavidsim.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class WeatherCache<T> {
    private static final long TTL_MINUTES = 10;
    private final ConcurrentHashMap<LocalDate, CacheEntry<T>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public WeatherCache() {
        cleaner.scheduleAtFixedRate(this::evictExpired, TTL_MINUTES, TTL_MINUTES, TimeUnit.MINUTES);
    }

    public T getOrLoad(LocalDate day, Supplier<T> loader) {
        return getCached(day).orElseGet(() -> {
            T fresh = loader.get();
            put(day, fresh);
            return fresh;
        });
    }

    public Optional<T> getCached(LocalDate day) {
        CacheEntry<T> entry = cache.get(day);
        if (entry == null || entry.isExpired(TTL_MINUTES)) {
            cache.remove(day);
            return Optional.empty();
        }
        return Optional.of(entry.getData());
    }

    public void put(LocalDate day, T data) {
        cache.put(day, new CacheEntry<>(data));
    }

    private void evictExpired() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired(TTL_MINUTES));
    }

    private static class CacheEntry<T> {
        private final T data;
        private final Instant timestamp;

        CacheEntry(T data) {
            this.data = data;
            this.timestamp = Instant.now();
        }

        boolean isExpired(long ttlMinutes) {
            return Duration.between(timestamp, Instant.now()).toMinutes() >= ttlMinutes;
        }

        T getData() {
            return data;
        }
    }
}
