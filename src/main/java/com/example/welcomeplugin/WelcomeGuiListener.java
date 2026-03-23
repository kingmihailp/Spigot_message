package com.example.welcomeplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class WelcomeGuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof WelcomeGuiHolder)) return;

        // Prevent taking any items
        event.setCancelled(true);

        // Close on button click
        if (event.getView().getTopInventory().equals(event.getClickedInventory())
                && event.getSlot() == WelcomeGui.BUTTON_SLOT
                && event.getWhoClicked() instanceof Player player) {
            player.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof WelcomeGuiHolder) {
            event.setCancelled(true);
        }
    }
}
