package com.michaeldavidsim.parks;

import java.awt.Color;

public class Park {
    private final String name;
    private final String baseUrl;
    private final String[] courtPaths;
    private final String[] regularRateKeys;
    private final String latitude;
    private final String longitude;
    private final Color color;

    public Park(String name, String baseUrl, String[] courtPaths, String[] regularRateKeys, String latitude, String longitude, Color color) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.courtPaths = courtPaths;
        this.regularRateKeys = regularRateKeys;
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String[] getCourtPaths() {
        return courtPaths;
    }

    public String[] getRegularRateKeys() {
        return regularRateKeys;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Color getColor() {
        return color;
    }
}
