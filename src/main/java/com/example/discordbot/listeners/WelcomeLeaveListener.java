package com.example.discordbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;

import java.awt.Color; // Correct Color import
import java.util.Date;

@Component
public class WelcomeLeaveListener extends ListenerAdapter {

    @Value("${JOIN_LEAVE_CHANNEL_ID:}")
    private String joinLeaveChannelId;

    // Properly injecting the footer icon URL via Spring
    @Value("${BOT_FOOTER_ICON_URL:}")
    private String botFooterIconUrl;

    /**
     * Handles the event when a member joins the guild (Welcome Message).
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (joinLeaveChannelId == null || joinLeaveChannelId.isEmpty()) return;

        TextChannel channel = event.getJDA().getTextChannelById(joinLeaveChannelId);

        if (channel != null) {
            String username = event.getUser().getName();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("🎉 Welcome to our server, " + username + "!")
                    .setDescription("We are glad to have you here.")
                    .setFooter("Welcome Message", botFooterIconUrl.isEmpty() ? null : botFooterIconUrl)
                    .setColor(Color.BLUE) // Using java.awt.Color
                    .setTimestamp(Instant.now());

            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    /*
     * Handles the event when a member leaves the guild (Leave Message).

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        if (joinLeaveChannelId == null || joinLeaveChannelId.isEmpty()) return;

        TextChannel channel = event.getJDA().getTextChannelById(joinLeaveChannelId);

        if (channel != null) {
            String username = event.getUser().getName();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("👋 Goodbye, " + username + ".")
                    .setDescription("We hope to see you again!")
                    .setFooter("Leave Message", botFooterIconUrl.isEmpty() ? null : botFooterIconUrl)
                    .setColor(Color.RED) // Using java.awt.Color
                    .setTimestamp(Instant.now());

            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }*/
}