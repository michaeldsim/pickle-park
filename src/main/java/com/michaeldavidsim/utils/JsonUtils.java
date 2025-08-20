package com.michaeldavidsim.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaeldavidsim.models.PickleResponse;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public static String parseTimes(String json, String[] allowedDescriptions) throws Exception {
        PickleResponse response = mapper.readValue(json, PickleResponse.class);

        if (response.hours == null) return "No data";

        return response.hours.stream()
                .filter(hour -> Arrays.asList(allowedDescriptions).contains(hour.description))
                .map(hour -> {
                    LocalTime start = LocalTime.parse(hour.startDtm.substring(11,16)); // Extract HH:mm
                    LocalTime end = LocalTime.parse(hour.endDtm.substring(11,16));
                    return " - " + start.format(TIME_FORMATTER) + " - " + end.format(TIME_FORMATTER);
                })
                .collect(Collectors.joining("\n"));
    }
}
