package com.michaeldavidsim;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.michaeldavidsim.models.openweathermodels.WeatherResponse;
import com.michaeldavidsim.parks.Park;
import com.michaeldavidsim.parks.ParkRegistry;
import com.michaeldavidsim.utils.AvailabilityCache;
import com.michaeldavidsim.utils.EmbedUtils;
import com.michaeldavidsim.utils.HttpUtils;
import com.michaeldavidsim.utils.JsonUtils;
import com.michaeldavidsim.utils.WeatherFetcher;
import com.michaeldavidsim.utils.WeatherService;
import org.natty.Parser;

public class PickleListener extends ListenerAdapter {

    private final HttpClient httpClient;
    private final WeatherService weatherService;
    private final Park park;

    public PickleListener(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.weatherService = new WeatherService(new WeatherFetcher(httpClient));
        this.park = ParkRegistry.EE_ROBINSON; // Default park for now
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();
        String prefix = System.getenv("PREFIX");
        if (!message.toLowerCase().startsWith(prefix)) return;

        String[] parts = message.split("\\s+", 2);
        LocalDate now = LocalDate.now();
        LocalDate targetDate = now;

        if (parts.length > 1) {
            Parser parser = new Parser(TimeZone.getTimeZone("America/New_York"));
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
        WeatherResponse weatherResponse = weatherService.getWeather(park.getLatitude(), park.getLongitude());

        new Thread(() -> fetchAndSendAvailability((TextChannel) event.getChannel(), finalTargetDate, weatherResponse, park)).start();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        if (!componentId.startsWith("park:")) {
            return;
        }

        // Parse button ID: "park:ParkName:2025-10-07"
        String[] parts = componentId.split(":", 3);
        if (parts.length != 3) {
            event.reply("Invalid button interaction").setEphemeral(true).queue();
            return;
        }

        String parkName = parts[1];
        LocalDate date = LocalDate.parse(parts[2]);

        // Find the park by name
        Park selectedPark = null;
        for (Park p : ParkRegistry.PARKS) {
            if (p.getName().equals(parkName)) {
                selectedPark = p;
                break;
            }
        }

        if (selectedPark == null) {
            event.reply("Park not found").setEphemeral(true).queue();
            return;
        }

        // Acknowledge the interaction immediately
        event.deferEdit().queue();

        final Park finalPark = selectedPark;
        WeatherResponse weatherResponse = weatherService.getWeather(finalPark.getLatitude(), finalPark.getLongitude());

        new Thread(() -> updateEmbedWithPark(event, date, weatherResponse, finalPark)).start();
    }

    private void updateEmbedWithPark(ButtonInteractionEvent event, LocalDate targetDate, WeatherResponse weatherResponse, Park park) {
        String[] times = AvailabilityCache.get(park.getName(), targetDate);

        if (times == null) {
            times = fetchAvailabilityData(park, targetDate);
            AvailabilityCache.put(park.getName(), targetDate, times);
        }

        EmbedUtils.updateCourtEmbed(event, targetDate, times, weatherResponse, park);
    }

    private void fetchAndSendAvailability(TextChannel channel, LocalDate targetDate, WeatherResponse weatherResponse, Park park) {
        String[] times = AvailabilityCache.get(park.getName(), targetDate);

        if (times == null) {
            times = fetchAvailabilityData(park, targetDate);
            AvailabilityCache.put(park.getName(), targetDate, times);
        }

        EmbedUtils.sendCourtEmbed(channel, targetDate, times, weatherResponse, park);
    }

    private String[] fetchAvailabilityData(Park park, LocalDate targetDate) {
        String[] times = new String[park.getCourtPaths().length];
        final int maxRetries = 3;
        final String id = generateId();

        for (int i = 0; i < park.getCourtPaths().length; i++) {
            String completeUrl = park.getBaseUrl() + id + park.getCourtPaths()[i] + targetDate.toString();
            int attempts = 0;
            boolean success = false;

            while (attempts < maxRetries && !success) {
                try {
                    String response = HttpUtils.getString(httpClient, completeUrl);
                    times[i] = JsonUtils.parseTimes(response, park.getRegularRateKeys());
                    success = true;
                } catch (Exception e) {
                    attempts++;
                    if (attempts >= maxRetries) {
                        times[i] = "Failed to retrieve data after " + maxRetries + " attempts";
                    }
                }
            }
        }

        return times;
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