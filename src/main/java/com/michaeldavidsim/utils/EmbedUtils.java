package com.michaeldavidsim.utils;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.EmbedBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.michaeldavidsim.models.openweathermodels.Daily;
import com.michaeldavidsim.models.openweathermodels.WeatherResponse;
import com.michaeldavidsim.utils.chart.WeatherChartRenderer;

import java.time.Instant;

import java.awt.Color;

public class EmbedUtils {
    private static final Logger logger = LoggerFactory.getLogger(EmbedUtils.class);

    public static void sendCourtEmbed(TextChannel channel, LocalDate date, String[] times, WeatherResponse response) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🎾 EE Robinson Pickleball Court Availability");
        embed.setColor(Color.GREEN);
        embed.setDescription("Availability for " + date.toString());

        for (int i = 0; i < times.length; i++) {
            embed.addField("Pickleball Court " + (i + 1), times[i], false);
        }

        Daily daily = getDailyForDate(response, date);

        if (daily != null) {
            embed.addField("🌡 Temperature", 
                String.format("High: %.0f°F\nLow: %.0f°F\nFeels Like: %.0f°F",
                    daily.getTemp().getMax(),
                    daily.getTemp().getMin(),
                    daily.getFeels_like().getDay()), true);

            embed.addField("💨 Wind", 
                String.format("%.0f mph", daily.getWind_speed()), true);

            embed.addField("💧 Humidity", 
                String.format("%d%%", daily.getHumidity()), true);

            embed.addField("☀️ UV Index", 
                String.format("%.0f", daily.getUvi()), true);

            embed.addField("🌧 Rain Chance", 
                String.format("%.0f%%", daily.getPop() * 100), true);
        } else {
            embed.addField("Weather Data", "No forecast available for this date", false);
        }

        // don't render the chart if after 10 since we filter out that data and we don't really care about it anyway
        LocalTime cutoff = LocalTime.of(22, 0);

        if (withinTwoDays(date) && LocalTime.now().isBefore(cutoff)) {
            try {
                byte[] bytes = WeatherChartRenderer.renderChart(response, date);
                FileUpload fileUpload = FileUpload.fromData(bytes, "rain_chart.png");

                embed.setImage("attachment://rain_chart.png");
                channel.sendMessageEmbeds(embed.build())
                    .addFiles(fileUpload)
                    .queue();
            } catch (IOException e) {
                logger.error("Error rendering chart: " + e.getMessage(), e);
                channel.sendMessageEmbeds(embed.build()).queue();
            }
        } else {
            embed.addField("Chart Data", "Chart is not rendered for dates after 2 days or after 10 PM", false);
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    private static Daily getDailyForDate(WeatherResponse response, LocalDate targetDate) {
        ZoneId zone = ZoneId.systemDefault();

        for (Daily daily : response.getDaily()) {
            LocalDate dailyDate = Instant.ofEpochSecond(daily.getDt())
                                        .atZone(zone)
                                        .toLocalDate();
            if (dailyDate.equals(targetDate)) {
                return daily;
            }
        }
        return null;
    }

    private static boolean withinTwoDays(LocalDate targetDate) {
        LocalDate today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(today, targetDate);
        return daysBetween <= 2;
    }
}
