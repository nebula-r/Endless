# Discord Bot Template (Java, Spring Boot, JDA)

A solid, scalable template for building Discord bots using [JDA (Java Discord API)](https://github.com/discord-jda/JDA) integrated with **Spring Boot**. The template also integrates common extensions for high-performant audio and databasing.

## Features

- **JDA 5.0+** integrated perfectly with Spring Boot via `@Bean`.
- **Slash Commands** (`ping`, `say`, `destruct`) using JDA's Interaction system.
- **RestActions** configured properly (using `.queue()`, `.flatMap()`, `.delay()`).
- **Lavaplayer** (`dev.arbjerg:lavaplayer`) pre-configured inside `BotConfig.java` to support audio playback.
- **JDA-NAS (UDPQueue)** configured out of the box using `setAudioSendFactory(new NativeAudioSendFactory())` to avoid GC pauses when sending audio.
- **Spring Data MongoDB** included for scaling a document datastore.
- **Dotenv (`.env`) System** allows tokens, credentials, and IDs to remain secure.

## Environment configuration

Before running the application, populate the `.env` file located in the root of the project:

```properties
DISCORD_TOKEN=your_bot_token_here
GUILD_ID=your_test_guild_id_here
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/discordbot
SPRING_DATA_MONGODB_DATABASE=discordbot
```

_Note: Remember to add `.env` to your `.gitignore`._

## Extensions & References

Included natively:

- **[Lavaplayer](https://github.com/lavalink-devs/lavaplayer)** - The standard java library for pulling music from YouTube/Soundcloud into Discord. Handled by the `AudioPlayerManager` Bean & `AudioPlayerSendHandler`.
- **[JDA-NAS (udpqueue)](https://github.com/sedmelluq/jda-nas)** - Native Audio Send System for ensuring audio playback doesn't stutter on GC cycles. We invoke this during the JDABuilder instantiation!

> **Note on JDA-KTX**: The user-requested extension `jda-ktx` is purely for **Kotlin**, so it's not built into this pure Java codebase, but is heavily recommended if you opt for Kotlin DSL in the future.

## Getting Started

1. Use Maven to install dependencies: `mvn clean install`
2. Start the MongoDB instance (e.g. running on `localhost:27017`).
3. Run `DiscordBotApplication.java` from the IDE.
4. Invite your Bot to the test server using its Oauth2 URL, granting `bot` & `applications.commands` scopes.
