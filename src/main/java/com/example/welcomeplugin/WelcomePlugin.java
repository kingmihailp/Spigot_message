package com.example.welcomeplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class WelcomePlugin extends JavaPlugin {

    private static WelcomePlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new WelcomeListener(this), this);

        getLogger().info("WelcomePlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WelcomePlugin disabled!");
    }

    public static WelcomePlugin getInstance() {
        return instance;
    }
}
