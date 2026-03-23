package com.example.welcomeplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class WelcomeListener implements Listener {

    private final WelcomePlugin plugin;
    private final WelcomeGui gui;

    public WelcomeListener(WelcomePlugin plugin) {
        this.plugin = plugin;
        this.gui = new WelcomeGui(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // 1-second delay so the client is fully loaded before opening the GUI
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> gui.open(player), 20L);
    }
}
