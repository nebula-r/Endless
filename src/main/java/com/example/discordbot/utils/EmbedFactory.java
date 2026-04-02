package com.example.discordbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EmbedFactory {

    @Value("${BOT_THUMBNAIL_URL:}")
    private String botThumbnailUrl;

    @Value("${BOT_ICON_URL:}")
    private String botIconUrl;

    @Value("${BOT_FOOTER_TEXT:}")
    private String botFooterText;

    /**
     * Creates a standardized EmbedBuilder with the predefined thumbnail, footer, and timestamp.
     */
    public EmbedBuilder createEmbed() {
        EmbedBuilder embed = new EmbedBuilder();

        // Add the standard thumbnail if present in .env
        if (botThumbnailUrl != null && !botThumbnailUrl.isEmpty()) {
            embed.setThumbnail(botThumbnailUrl.replace("\"", ""));
        }

        // Add the standard footer if present in .env
        if (botFooterText != null && !botFooterText.isEmpty()) {
            String cleanFooter = botFooterText.replace("\"", "");
            if (botIconUrl != null && !botIconUrl.isEmpty()) {
                embed.setFooter(cleanFooter, botIconUrl.replace("\"", ""));
            } else {
                embed.setFooter(cleanFooter);
            }
        }

        embed.setTimestamp(Instant.now());
        return embed;
    }
}

