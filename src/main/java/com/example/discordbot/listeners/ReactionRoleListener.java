package com.example.discordbot.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@Component
public class ReactionRoleListener extends ListenerAdapter {

    @Value("${REACTION_ROLES_CHANNEL_ID:}")
    private String channelId;

    @Value("${REACTION_ROLES_14_PLUS_ID:}")
    private String role14PlusId;

    @Value("${REACTION_ROLES_16_PLUS_ID:}")
    private String role16PlusId;

    @Value("${REACTION_ROLES_18_PLUS_ID:}")
    private String role18PlusId;

    @Value("${REACTION_ROLES_20_PLUS_ID:}")
    private String role20PlusId;

    @Value("${REACTION_ROLES_MINECRAFT_ID:}")
    private String roleMinecraftId;

    @Value("${REACTION_ROLES_ARTIST_ID:}")
    private String roleArtistId;

    @Value("${REACTION_ROLES_PRODUCER_ID:}")
    private String roleProducerId;

    @Value("${REACTION_ROLES_SINGER_ID:}")
    private String roleSingerId;

    @Value("${REACTION_ROLES_BEATBOXER_ID:}")
    private String roleBeatboxerId;

    @Value("${REACTION_ROLES_LOOKING_FOR_VOCALIST_ID:}")
    private String roleLookingForVocalistId;

    @Value("${REACTION_ROLES_LOOKING_FOR_PRODUCER_ID:}")
    private String roleLookingForProducerId;

    @Value("${REACTION_ROLES_GAMER_ID:}")
    private String roleGamerId;

    @Value("${REACTION_ROLES_OTAKU_ID:}")
    private String roleOtakuId;

    @Value("${REACTION_ROLES_VR_ID:}")
    private String roleVrId;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if (channelId == null || channelId.isEmpty()) return;

        TextChannel channel = event.getJDA().getTextChannelById(channelId);
        if (channel == null) {
            System.err.println("Cannot find channel with ID " + channelId);
            return;
        }

        try {
            MessageHistory history = channel.getHistory();
            List<Message> messages = history.retrievePast(10).complete();
            List<Message> botMessages = messages.stream()
                    .filter(msg -> msg.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
                    .collect(Collectors.toList());

            if (!botMessages.isEmpty()) {
                if (botMessages.size() > 1) {
                    channel.deleteMessages(botMessages).queue();
                } else {
                    botMessages.get(0).delete().queue();
                }
                System.out.println(botMessages.size() + " old bot messages deleted.");
            }
        } catch (Exception e) {
            System.err.println("Error deleting old messages: " + e.getMessage());
        }

        ActionRow ageSelectionRow = ActionRow.of(
            Button.success("14plus", "14+").withEmoji(Emoji.fromUnicode("🔞")),
            Button.success("16plus", "16+").withEmoji(Emoji.fromUnicode("🔞")),
            Button.danger("18plus", "18+").withEmoji(Emoji.fromUnicode("🔞")),
            Button.danger("20plus", "20+").withEmoji(Emoji.fromUnicode("🔞"))
        );

        ActionRow row = ActionRow.of(
            Button.secondary("minecraft", "Minecraft").withEmoji(Emoji.fromUnicode("🗡️")),
            Button.secondary("artist", "Artist").withEmoji(Emoji.fromUnicode("🎨")),
            Button.secondary("producer", "Producer").withEmoji(Emoji.fromUnicode("🎧")),
            Button.secondary("singer", "Singer").withEmoji(Emoji.fromUnicode("🎤")),
            Button.secondary("beatboxer", "Beatboxer").withEmoji(Emoji.fromUnicode("🎤"))
        );

        ActionRow row2 = ActionRow.of(
            Button.secondary("lookingforvocalist", "Looking for Vocalist").withEmoji(Emoji.fromUnicode("🎤")),
            Button.secondary("lookingforproducer", "Looking for Producer").withEmoji(Emoji.fromUnicode("🎧")),
            Button.secondary("gamer", "Gamer").withEmoji(Emoji.fromUnicode("🎮")),
            Button.secondary("otaku", "Otaku").withEmoji(Emoji.fromUnicode("👾")),
            Button.secondary("vr", "VR").withEmoji(Emoji.fromUnicode("🕶️"))
        );

        channel.sendMessage("Select your role by clicking on a button")
               .addComponents(ageSelectionRow, row, row2)
               .queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String customId = event.getComponentId();
        String roleId = switch (customId.toLowerCase()) {
            case "14plus" -> role14PlusId;
            case "16plus" -> role16PlusId;
            case "18plus" -> role18PlusId;
            case "20plus" -> role20PlusId;
            case "minecraft" -> roleMinecraftId;
            case "artist" -> roleArtistId;
            case "producer" -> roleProducerId;
            case "singer" -> roleSingerId;
            case "beatboxer" -> roleBeatboxerId;
            case "lookingforvocalist" -> roleLookingForVocalistId;
            case "lookingforproducer" -> roleLookingForProducerId;
            case "gamer" -> roleGamerId;
            case "otaku" -> roleOtakuId;
            case "vr" -> roleVrId;
            default -> null;
        };

        if (roleId == null || roleId.isEmpty()) {
            return;
        }

        if (event.getGuild() == null) {
            event.reply("This must be used in a server").setEphemeral(true).queue();
            return;
        }

        Role role = event.getGuild().getRoleById(roleId);
        if (role == null) {
            event.reply("Role not found").setEphemeral(true).queue();
            return;
        }

        if (event.getMember() == null) {
            event.reply("Member not found").setEphemeral(true).queue();
            return;
        }

        boolean hasRole = event.getMember().getRoles().contains(role);

        if (hasRole) {
            event.getGuild().removeRoleFromMember(event.getMember(), role).queue(
                success -> event.reply("The " + role.getAsMention() + " role was removed to you " + event.getMember().getAsMention()).setEphemeral(true).queue(),
                error -> {
                    error.printStackTrace();
                    event.reply("Something went wrong. The " + role.getAsMention() + " role was not removed to you " + event.getMember().getAsMention()).setEphemeral(true).queue();
                }
            );
        } else {
            event.getGuild().addRoleToMember(event.getMember(), role).queue(
                success -> event.reply("The " + role.getAsMention() + " role was added to you " + event.getMember().getAsMention()).setEphemeral(true).queue(),
                error -> {
                    error.printStackTrace();
                    event.reply("Something went wrong. The " + role.getAsMention() + " role was not added to you " + event.getMember().getAsMention()).setEphemeral(true).queue();
                }
            );
        }
    }
}
