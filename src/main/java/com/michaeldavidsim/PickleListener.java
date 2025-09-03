package com.michaeldavidsim;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.Parser;
import com.michaeldavidsim.models.openweathermodels.WeatherResponse;
import com.michaeldavidsim.utils.EmbedUtils;
import com.michaeldavidsim.utils.HttpUtils;
import com.michaeldavidsim.utils.JsonUtils;
import com.michaeldavidsim.utils.WeatherFetcher;
import com.michaeldavidsim.utils.WeatherService;

public class PickleListener extends ListenerAdapter {

    private final HttpClient httpClient;
    private final WeatherService weatherService;
    private final String baseUrl = "https://secure.rec1.com/GA/gwinnett-county-parks-recreation/catalog/getFacilityHours/";
    private final String[] eeRobinsonCourts = {"/92843/665450/", "/92844/665452/", "/93224/665454/", "/93225/665456/", "/141095/665468/", "/141096/665470/"};
    private final String[] regularRateKeys = {"Pickleball $5", "Pickleball EE Robinson"};
    private final String LATITUDE = "34.0977";
    private final String LONGITUDE = "-84.0429";

    public PickleListener(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.weatherService = new WeatherService(new WeatherFetcher(httpClient));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();
        if (!message.toLowerCase().startsWith("!pp")) return;

        String[] parts = message.split("\\s+", 2);
        LocalDate now = LocalDate.now();
        LocalDate targetDate = now;

        if (parts.length > 1) {
            Parser parser = new Parser();
            List<Date> dates = parser.parse(parts[1]).stream()
                                    .flatMap(group -> group.getDates().stream())
                                    .toList();

            if (!dates.isEmpty()) {
                targetDate = dates.get(0).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                event.getChannel().sendMessage("⚠️ Could not parse the date. Try something like 08-20 or 'tomorrow'.").queue();
                return;
            }
        }

        if (targetDate.isBefore(now)) {
            event.getChannel().sendMessage("⚠️ Date must be today or in the future.").queue();
            return;
        }
        if (targetDate.isAfter(now.plusDays(8))) {
            event.getChannel().sendMessage("❗ Date is more than 8 days from now. Reservations only allowed within 8 days.").queue();
            return;
        }

        final LocalDate finalTargetDate = targetDate;
        WeatherResponse weatherResponse = weatherService.getWeather(LATITUDE, LONGITUDE);
        
        new Thread(() -> fetchAndSendAvailability((TextChannel) event.getChannel(), finalTargetDate, weatherResponse)).start();
    }


    private void fetchAndSendAvailability(TextChannel channel, LocalDate targetDate, WeatherResponse weatherResponse) {
        String[] times = new String[eeRobinsonCourts.length];
        final int maxRetries = 3;
        final String id = generateId();

        for (int i = 0; i < eeRobinsonCourts.length; i++) {
            String completeUrl = baseUrl + id + eeRobinsonCourts[i] + targetDate.toString();
            int attempts = 0;
            boolean success = false;

            while (attempts < maxRetries && !success) {
                try {
                    String response = HttpUtils.getString(httpClient, completeUrl);
                    times[i] = JsonUtils.parseTimes(response, regularRateKeys);
                    success = true;
                } catch (Exception e) {
                    attempts++;
                    if (attempts >= maxRetries) {
                        times[i] = "Failed to retrieve data after " + maxRetries + " attempts";
                        e.printStackTrace();
                    }
                }
            }
        }

        EmbedUtils.sendCourtEmbed(channel, targetDate, times, weatherResponse);
    }


    private static String generateId() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            id.append(characters.charAt((int)(Math.random() * characters.length())));
        }
        return id.toString();
    }
}