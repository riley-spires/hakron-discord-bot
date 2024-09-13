package com.hakron;


import com.hakron.discordbot.DiscordBot;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        DiscordBot.getInstance();

        System.out.println("Bot is running!");
    }
}