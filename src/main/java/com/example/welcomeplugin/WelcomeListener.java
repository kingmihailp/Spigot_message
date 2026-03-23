package com.example.welcomeplugin;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class WelcomeListener implements Listener {

    private final WelcomePlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public WelcomeListener(WelcomePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Small delay so the client is fully loaded before opening the book
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> openWelcomeBook(player), 20L);
    }

    private void openWelcomeBook(Player player) {
        List<String> lines = plugin.getConfig().getStringList("message.lines");
        String buttonText = plugin.getConfig().getString("message.button.text", "[ Понятно ]");
        String buttonColor = plugin.getConfig().getString("message.button.color", "#55FF55");
        String hoverText = plugin.getConfig().getString("message.button.hover-text", "Нажмите, чтобы закрыть");

        // Build page content from configured lines
        Component pageContent = Component.empty();
        for (String line : lines) {
            if (line.isEmpty()) {
                pageContent = pageContent.append(Component.newline());
            } else {
                pageContent = pageContent
                        .append(miniMessage.deserialize(line))
                        .append(Component.newline());
            }
        }

        // Resolve button color (hex string or fallback)
        TextColor btnColor;
        try {
            btnColor = buttonColor.startsWith("#")
                    ? TextColor.fromHexString(buttonColor)
                    : TextColor.fromHexString("#55FF55");
        } catch (Exception e) {
            btnColor = TextColor.fromHexString("#55FF55");
        }

        // Build the clickable "Got it" button
        Component button = Component.text(buttonText)
                .color(btnColor)
                .clickEvent(ClickEvent.runCommand("/welcome_ack"))
                .hoverEvent(HoverEvent.showText(
                        miniMessage.deserialize("<gray>" + hoverText + "</gray>")));

        pageContent = pageContent.append(Component.newline()).append(button);

        // Open the book screen for the player
        Book book = Book.builder()
                .title(Component.text("Welcome"))
                .author(Component.text("Server"))
                .addPage(pageContent)
                .build();

        player.openBook(book);
    }
}
