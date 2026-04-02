package com.example.discordbot.tasks;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.context.annotation.Lazy;

@Component
public class GreetingTasks {

    private final JDA jda;

    @Value("${GENERAL_CHANNEL_ID}")
    private String generalChannelId;

    private final List<String> morningGifs = Arrays.asList(
            "https://tenor.com/view/hello-kitty-my-melody-melody-tea-tea-cup-gif-4255970539023848420",
            "https://tenor.com/view/rwby-rwbypenny-penny-pennypolendina-goodmorning-gif-16058203",
            "https://tenor.com/view/rwby-rwby-icequeendom-ruby-gm-wake-up-gif-26047033",
            "https://tenor.com/view/morning-anime-elf-frieren-gif-4413475363987094937",
            "https://tenor.com/view/anime-anime-good-morning-good-morning-good-morning-anime-gif-4861453462456054543"
    );

    private final List<String> nightGifs = Arrays.asList(
            "https://tenor.com/view/ai-hoshino-wink-cute-good-night-gif-2597008239877351815",
            "https://tenor.com/view/rwby-good-night-weiss-schnee-yang-xiao-long-blake-belladonna-gif-12449215722549532930",
            "https://tenor.com/view/goodnight-good-night-kuromi-sleeping-onegai-my-melody-gif-3783711001948231937",
            "https://tenor.com/view/sanrio-discord-good-night-good-night-chat-my-melody-gif-27233647"
    );

    private final Random random = new Random();

    public GreetingTasks(@Lazy JDA jda) {
        this.jda = jda;
    }

    // Runs every day at 8:00 AM Europe/Berlin time. Adjust the cron expression as needed.
    @Scheduled(cron = "0 0 8 * * *", zone = "Europe/Berlin")
    public void sendGoodMorning() {
        if (generalChannelId == null || generalChannelId.isEmpty()) {
            return;
        }

        TextChannel channel = jda.getTextChannelById(generalChannelId);
        if (channel != null) {
            String randomGif = morningGifs.get(random.nextInt(morningGifs.size()));
            channel.sendMessage(randomGif).queue();
        }
    }

    // Runs every day at 10:00 PM (22:00) Europe/Berlin time.
    @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Berlin")
    public void sendGoodNight() {
        if (generalChannelId == null || generalChannelId.isEmpty()) {
            return;
        }

        TextChannel channel = jda.getTextChannelById(generalChannelId);
        if (channel != null) {
            String randomGif = nightGifs.get(random.nextInt(nightGifs.size()));
            channel.sendMessage(randomGif).queue();
        }
    }
}
