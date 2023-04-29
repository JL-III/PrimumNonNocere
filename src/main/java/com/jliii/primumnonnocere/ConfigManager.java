package com.jliii.primumnonnocere;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    PrimumNonNocere plugin;
    FileConfiguration config;
    List<String> softMutedPlayers;

    public ConfigManager(PrimumNonNocere plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        this.softMutedPlayers = config.getStringList("soft-muted-players");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        softMutedPlayers = config.getStringList("soft-muted-players");
    }

    public void saveConfig() {
        config.set("soft-muted-players", softMutedPlayers);
        plugin.saveConfig();
    }


    public List<String> getSoftMutedPlayers() {
        return softMutedPlayers;
    }
}
