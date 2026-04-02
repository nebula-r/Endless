package com.example.discordbot.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.Permission;

import com.example.discordbot.utils.EmbedFactory;
import com.example.discordbot.tasks.GreetingTasks;

@Component
public class SlashCommandListener extends ListenerAdapter {

    @Value("${BOT_CHANNEL_ID:}")
    private String botChannelId;

    private final EmbedFactory embedFactory;
    private final GreetingTasks greetingTasks;

    public SlashCommandListener(EmbedFactory embedFactory, GreetingTasks greetingTasks) {
        this.embedFactory = embedFactory;
        this.greetingTasks = greetingTasks;
    }

    @Override
    public void onReady(ReadyEvent event) {
        // Registering slash commands globally or to a specific guild
        event.getJDA().updateCommands().addCommands(
                Commands.slash("ping", "Calculate bot ping"),
                Commands.slash("say", "Makes the bot say what you tell it to")
                        .addOption(OptionType.STRING, "content", "What the bot should say", true),
                Commands.slash("destruct", "Sends a message that deletes itself in 5 seconds"),
                Commands.slash("testgreeting", "Trigger the morning and night greeting task for testing")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))).queue();

        if (botChannelId != null && !botChannelId.isEmpty()) {
            try {
                TextChannel channel = event.getJDA().getTextChannelById(botChannelId);
                if (channel != null) {
                    EmbedBuilder byeEmbed = embedFactory.createEmbed()
                            .setColor(Color.RED) // '#f00000' equivalent
                            .setTitle("**" + event.getJDA().getSelfUser().getName() + "** is back online!**");

                    channel.sendMessageEmbeds(byeEmbed.build()).queue();
                }
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String botName = event.getJDA().getSelfUser().getName();
        String botAvatar = event.getJDA().getSelfUser().getAvatarUrl();

        switch (event.getName()) {
            case "testgreeting" -> {
                if (event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.reply("You do not have permission to use this command.").setEphemeral(true).queue();
                    return;
                }
                greetingTasks.sendGoodMorning();
                greetingTasks.sendGoodNight();
                event.reply("Sent morning and night greetings!").setEphemeral(true).queue();
            }
            case "ping" -> {
                long ping = event.getJDA().getGatewayPing();
                EmbedBuilder embed = embedFactory.createEmbed()
                        .setAuthor(botName, null, botAvatar)
                        .setDescription("🏓 Pong! \n 📡 Ping: " + ping + "ms")
                        .setColor(Color.GREEN);

                event.replyEmbeds(embed.build()).queue();
            }
            case "say" -> {
                OptionMapping contentOption = event.getOption("content");
                String content = contentOption != null ? contentOption.getAsString() : "Hello!";
                EmbedBuilder embed = embedFactory.createEmbed()
                        .setAuthor(botName, null, botAvatar)
                        .setDescription(content)
                        .setColor(Color.CYAN);

                event.replyEmbeds(embed.build()).queue();
            }
            case "destruct" -> {
                EmbedBuilder embed = embedFactory.createEmbed()
                        .setAuthor(botName, null, botAvatar)
                        .setDescription("This message will self-destruct in 5 seconds...")
                        .setColor(Color.RED);

                // Example of chained RestActions
                event.replyEmbeds(embed.build())
                        .setEphemeral(false)
                        .delay(5, TimeUnit.SECONDS)
                        .flatMap(InteractionHook::deleteOriginal)
                        .queue();
            }
        }
    }
}
