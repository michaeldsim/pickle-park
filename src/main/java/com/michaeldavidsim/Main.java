package com.michaeldavidsim;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.net.http.HttpClient;

import javax.security.auth.login.LoginException;

import com.michaeldavidsim.utils.HttpUtils;

public class Main {
    public static void main(String[] args) throws LoginException {
        String token = System.getenv("DISCORD_BOT_TOKEN");

        HttpClient httpClient = HttpUtils.createHttpClient();

        if (token == null || token.isEmpty()) {
            System.err.println("Please set the DISCORD_BOT_TOKEN environment variable.");
            return;
        }

        String prefix = System.getenv("PREFIX");

        JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Type " + prefix))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .addEventListeners(new PickleListener(httpClient))
                .build();

        System.out.println("Bot is running!");
    }
}
