package com.example.welcomeplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class WelcomeGui {

    /** Slot index of the "Got it" button (center of bottom row in a 4-row chest). */
    static final int BUTTON_SLOT = 31;

    /**
     * Inner text slots: rows 1–2 of a 4-row chest, excluding left/right borders.
     * Layout (4 rows × 9 cols = 36 slots):
     *   Row 0 (0–8):   border
     *   Row 1 (9–17):  border | text x7 | border
     *   Row 2 (18–26): border | text x7 | border
     *   Row 3 (27–35): border | border | border | border | BUTTON | border | border | border | border
     */
    private static final int[] TEXT_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25
    };

    private final WelcomePlugin plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public WelcomeGui(WelcomePlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        String titleStr   = plugin.getConfig().getString("message.title",       "<gold>Добро пожаловать!</gold>");
        List<String> lines = plugin.getConfig().getStringList("message.lines");
        String buttonText  = plugin.getConfig().getString("message.button.text",  "✔ Понятно");
        String buttonColor = plugin.getConfig().getString("message.button.color", "#55FF55");

        // Create 4-row inventory with the configured title
        Inventory inv = Bukkit.createInventory(new WelcomeGuiHolder(), 36, mm.deserialize(titleStr));

        // Fill border
        ItemStack border = makeBorder();
        for (int i = 0; i < 9; i++)  inv.setItem(i, border);       // top row
        for (int i = 27; i < 36; i++) inv.setItem(i, border);      // bottom row
        inv.setItem(9,  border); inv.setItem(17, border);           // sides row 1
        inv.setItem(18, border); inv.setItem(26, border);           // sides row 2

        // Place text lines
        for (int i = 0; i < lines.size() && i < TEXT_SLOTS.length; i++) {
            String line = lines.get(i);
            if (!line.isEmpty()) {
                inv.setItem(TEXT_SLOTS[i], makeTextItem(mm.deserialize(line)));
            }
        }

        // Place button
        Component btnLabel = Component.text(buttonText).color(parseColor(buttonColor));
        inv.setItem(BUTTON_SLOT, makeButtonItem(btnLabel));

        player.openInventory(inv);
    }

    // ---- item factories ----

    private ItemStack makeBorder() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.empty());
        meta.setHideTooltip(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack makeTextItem(Component text) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(text);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack makeButtonItem(Component label) {
        ItemStack item = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(label);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private TextColor parseColor(String hex) {
        try {
            return hex.startsWith("#") ? TextColor.fromHexString(hex) : TextColor.fromHexString("#55FF55");
        } catch (Exception e) {
            return TextColor.fromHexString("#55FF55");
        }
    }
}
