package com.example.discordbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserProfile {

    @Id
    private String discordId;
    private int commandUses;

    public UserProfile() {
    }

    public UserProfile(String discordId) {
        this.discordId = discordId;
        this.commandUses = 0;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public int getCommandUses() {
        return commandUses;
    }

    public void incrementCommandUses() {
        this.commandUses++;
    }
}
