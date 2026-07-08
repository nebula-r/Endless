package com.example.discordbot.listeners;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WelcomeLeaveListener extends ListenerAdapter {

    // Assuming these are configured in example.env and injected via Spring
    @Value("${JOIN_LEAVE_CHANNEL_ID:}")
    private String joinLeaveChannelId;

    /**
     * Handles the event when a member joins the guild (Welcome Message).
     */
    public void onGuildJoin(GuildJoinEvent event) {
        if (joinLeaveChannelId == null || joinLeaveChannelId.isEmpty()) return;

        // Retrieve the target channel safely and check for null
        net.dv8tion.jda.api.entities.channel.concrete.TextChannel channel = event.getJDA().getTextChannelById(joinLeaveChannelId);

        if (channel != null) {
            String welcomeMessage = "🎉 Welcome to our server! We are glad to have you here.";
            channel.sendMessage(welcomeMessage).queue();
        }
    }

    /**
     * Handles the event when a member leaves the guild (Leave Message).
     */
    public void onGuildLeave(GuildLeaveEvent event) {
        if (joinLeaveChannelId == null || joinLeaveChannelId.isEmpty()) return;

        // Retrieve the target channel safely and check for null
        net.dv8tion.jda.api.entities.channel.concrete.TextChannel channel = event.getJDA().getTextChannelById(joinLeaveChannelId);

        if (channel != null) {
            String leaveMessage = "👋 Goodbye. We hope to see you again!";
            // You can customize the embed/message based on your ENV variables (e.g., JOIN_LEAVE_AUTHOR_NAME)
            channel.sendMessage(leaveMessage).queue();
        }
    }
}