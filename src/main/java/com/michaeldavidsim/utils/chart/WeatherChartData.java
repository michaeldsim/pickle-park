package com.michaeldavidsim.utils.chart;

import com.michaeldavidsim.models.openweathermodels.Hourly;
import com.michaeldavidsim.models.openweathermodels.WeatherResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class WeatherChartData {
    public static List<String> getHourLabels(WeatherResponse response, LocalDate targetDate) {
        return response.getHourly().stream()
                .map(h -> Instant.ofEpochSecond(h.getDt())
                        .atZone(ZoneId.of(response.getTimezone())))
                .filter(time -> time.toLocalDate().equals(targetDate))

                .filter(time -> time.getHour() >= 7 && time.getHour() <= 22)
                .map(time -> {
                    int hour = time.getHour();
                    String label = hour % 12 == 0 ? "12" : String.valueOf(hour % 12);
                    return label + (hour < 12 ? "AM" : "PM");
                })
                .collect(Collectors.toList());
    }

    public static List<Double> getRainPercentages(WeatherResponse response, LocalDate targetDate) {
        return response.getHourly().stream()
                .map(h -> new Object[] {
                        h,
                        Instant.ofEpochSecond(h.getDt()).atZone(ZoneId.of(response.getTimezone()))
                })
                .filter(arr -> {
                    ZonedDateTime time = (ZonedDateTime) arr[1];
                    return time.toLocalDate().equals(targetDate)
                            && time.getHour() >= 7
                            && time.getHour() <= 22;
                })
                .map(arr -> {
                    Hourly h = (Hourly) arr[0];
                    return h.getPop() * 100;
                })
                .collect(Collectors.toList());
    }
}
