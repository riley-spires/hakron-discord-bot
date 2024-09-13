package com.hakron.discordbot;

import com.hakron.Helpers;
import com.hakron.discordbot.listeners.commands.SetHexCode;
import lombok.Getter;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.ArrayList;

public class DiscordBot {
    private static DiscordBot instance;

    @Getter
    private final DiscordApi api;
    private final ArrayList<SlashCommand> slashCommands = new ArrayList<>();

    private DiscordBot() throws Exception {
        api = new DiscordApiBuilder()
                .setToken(Helpers.getEnv("DISCORD_TOKEN"))
                .setIntents(Intent.MESSAGE_CONTENT)
                .login()
                .join();

        SlashCommandBuilder builder = SlashCommand.with("set-hex", "Set your name to the provided hex code");

        builder.addOption(SlashCommandOption.createStringOption("HexCode", "The hex code to change your name color to starting with #", true));

        builder.createGlobal(api).join();

        api.addInteractionCreateListener(new SetHexCode());
    }

    public static DiscordBot getInstance() throws Exception {
        if (instance == null) {
            instance = new DiscordBot();
        }
        return instance;
    }
}
