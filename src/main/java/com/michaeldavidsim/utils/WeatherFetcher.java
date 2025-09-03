package com.michaeldavidsim.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaeldavidsim.models.openweathermodels.WeatherResponse;

public class WeatherFetcher {
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = "https://api.openweathermap.org/data/3.0/onecall";
    private final String apiKey = System.getenv("WEATHER_API_KEY");

    public WeatherFetcher(HttpClient client) {
        this.client = client;
    }

    public WeatherResponse fetch(String latitude, String longitude) throws IOException, InterruptedException {
        String query = String.format("?lat=%s&lon=%s&appid=%s&units=imperial", latitude, longitude, apiKey);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + query))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), WeatherResponse.class);
    }
}
