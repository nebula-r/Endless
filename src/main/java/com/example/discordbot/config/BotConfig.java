package com.example.discordbot.config;

import com.example.discordbot.listeners.SlashCommandListener;
import com.example.discordbot.listeners.ReactionRoleListener;
import com.example.discordbot.listeners.WelcomeLeaveListener;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
public class BotConfig {

    @Value("${DISCORD_TOKEN}")
    private String token;

    private int timerCount = 0;

    @Bean
    public JDA jda(SlashCommandListener slashCommandListener, ReactionRoleListener reactionRoleListener, WelcomeLeaveListener welcomeLeaveListener) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token, EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MEMBERS))
                .setActivity(Activity.watching("Watching over the server"))
                .addEventListeners(slashCommandListener, reactionRoleListener, welcomeLeaveListener)
                .build();

        jda.awaitReady();

        // Add a scheduled timer to update the presence periodically
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            timerCount++;
            jda.getPresence().setActivity(Activity.watching("Watching over the server for " + timerCount + " minutes"));
        }, 1, 1, TimeUnit.MINUTES);

        return jda;
    }

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        return playerManager;
    }
}
