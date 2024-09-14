package com.hakron.discordbot.listeners.commands;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.RoleBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.InteractionCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.listener.interaction.InteractionCreateListener;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetHexCode implements InteractionCreateListener {
    @Override
    public void onInteractionCreate(InteractionCreateEvent event) {
        Optional<SlashCommandInteraction> interactionOptional = event.getSlashCommandInteraction();
        if (interactionOptional.isEmpty()) return;

        SlashCommandInteraction interaction = interactionOptional.get();
        if (!interaction.getFullCommandName().equals("set-hex")) return;

        Optional<SlashCommandInteractionOption> hexCodeOptionOptional = interaction.getOptionByName("HexCode");
        if (hexCodeOptionOptional.isEmpty()) {
            interaction.createImmediateResponder().setContent("You didn't provide a hex code!").respond();
            return;
        }

        Optional<String> hexCodeOptional = hexCodeOptionOptional.get().getStringValue();
        if (hexCodeOptional.isEmpty()) {
            interaction.createImmediateResponder().setContent("You didn't provide a hex code!");
            return;
        }
        String hexCode = hexCodeOptional.get();

        String regexPattern = "^#([A-Fa-f0-9]{6})$";

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(hexCode);

        if (!matcher.matches()) {
            interaction.createImmediateResponder().setContent("You entered an invalid hex code!").respond();
            return;
        }

        int red = Integer.parseInt(hexCode.substring(1, 3), 16);
        int green = Integer.parseInt(hexCode.substring(3, 5), 16);
        int blue = Integer.parseInt(hexCode.substring(5, 7), 16);

        Color color = new Color(red, green, blue);

        final String userID = interaction.getUser().getIdAsString();

        Optional<Server> serverOptional = interaction.getServer();
        if (serverOptional.isEmpty()) return;
        Server server = serverOptional.get();

        if (server.getRolesByName(userID).isEmpty()) {
            RoleBuilder roleBuilder = new RoleBuilder(server);

            roleBuilder.setColor(color);
            roleBuilder.setName(userID);
            Role role = roleBuilder.create().join();

            interaction.getUser().addRole(role).join();
        } else {
            Role role = server.getRolesByName(userID).getFirst();

            role.updateColor(color);
        }


        interaction.createImmediateResponder().setContent("Your user color is now: %s".formatted(hexCode)).respond();
    }
}
