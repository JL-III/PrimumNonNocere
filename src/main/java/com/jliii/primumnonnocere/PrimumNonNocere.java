package com.jliii.primumnonnocere;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PrimumNonNocere extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigManager configManager = new ConfigManager(this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(configManager), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("tchat")).setExecutor(new AdminCommands(configManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
