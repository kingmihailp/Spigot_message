package com.example.welcomeplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        List<String> lines = plugin.getConfig().getStringList("message.lines");
        for (String line : lines) {
            if (line.isEmpty()) {
                player.sendMessage(Component.empty());
            } else {
                player.sendMessage(mm.deserialize(line));
            }
        }
    }
}
