package com.michaeldavidsim.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AvailabilityCache {
    private static final long TTL_SECONDS = 15;
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private static class CacheEntry {
        final String[] times;
        final Instant expiresAt;

        CacheEntry(String[] times) {
            this.times = times;
            this.expiresAt = Instant.now().plusSeconds(TTL_SECONDS);
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }

    public static String[] get(String parkName, LocalDate date) {
        String key = generateKey(parkName, date);
        CacheEntry entry = cache.get(key);

        if (entry != null && !entry.isExpired()) {
            return entry.times;
        }

        if (entry != null) {
            cache.remove(key);
        }

        return null;
    }

    public static void put(String parkName, LocalDate date, String[] times) {
        String key = generateKey(parkName, date);
        cache.put(key, new CacheEntry(times));
    }

    private static String generateKey(String parkName, LocalDate date) {
        return parkName + ":" + date.toString();
    }

    public static void clear() {
        cache.clear();
    }
}
