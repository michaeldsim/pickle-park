package com.michaeldavidsim.utils;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import java.time.LocalDate;
import java.awt.Color;

public class EmbedUtils {
    public static void sendCourtEmbed(TextChannel channel, LocalDate date, String[] times) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🎾 EE Robinson Pickleball Court Availability");
        embed.setColor(Color.GREEN);
        embed.setDescription("Availability for " + date.toString());

        for (int i = 0; i < times.length; i++) {
            embed.addField("Pickleball Court " + (i + 1), times[i], false);
        }

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
