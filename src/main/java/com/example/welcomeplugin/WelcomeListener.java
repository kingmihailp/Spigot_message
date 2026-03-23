package com.example.welcomeplugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;
import java.util.List;

public class WelcomeListener implements Listener {

    private final WelcomePlugin plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public WelcomeListener(WelcomePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> showWelcome(player), 20L);
    }

    private void showWelcome(Player player) {
        String titleStr    = plugin.getConfig().getString("message.title",    "<gold><bold>Добро пожаловать!</bold></gold>");
        String subtitleStr = plugin.getConfig().getString("message.subtitle", "");
        int fadeIn  = plugin.getConfig().getInt("message.fade-in", 10);
        int stay    = plugin.getConfig().getInt("message.stay",    70);
        int fadeOut = plugin.getConfig().getInt("message.fade-out", 20);

        // Show title on screen
        Title title = Title.title(
            mm.deserialize(titleStr),
            subtitleStr.isEmpty() ? net.kyori.adventure.text.Component.empty() : mm.deserialize(subtitleStr),
            Title.Times.times(
                Duration.ofMillis(fadeIn  * 50L),
                Duration.ofMillis(stay    * 50L),
                Duration.ofMillis(fadeOut * 50L)
            )
        );
        player.showTitle(title);

        // Send chat lines
        List<String> lines = plugin.getConfig().getStringList("message.lines");
        for (String line : lines) {
            if (line.isEmpty()) {
                player.sendMessage(net.kyori.adventure.text.Component.empty());
            } else {
                player.sendMessage(mm.deserialize(line));
            }
        }
    }
}
