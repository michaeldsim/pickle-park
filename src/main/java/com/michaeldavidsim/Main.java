package com.michaeldavidsim;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        String token = System.getenv("DISCORD_BOT_TOKEN");

        if (token == null || token.isEmpty()) {
            System.err.println("Please set the DISCORD_BOT_TOKEN environment variable.");
            return;
        }

        JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Type !pp"))
                .addEventListeners(new PickleListener())
                .build();

        System.out.println("Bot is running!");
    }
}
