package com.jliii.primumnonnocere.managers;

import com.jliii.primumnonnocere.PrimumNonNocere;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.ChatColor;
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

    public void addOverNightSoftMutedPlayers() {
        List<String> overNightSoftMutedPlayers = config.getStringList("overnight-soft-mutes");
        softMutedPlayers.addAll(overNightSoftMutedPlayers);
        GeneralUtils.pluginLogger(ChatColor.GREEN, "Added " + overNightSoftMutedPlayers.size() + " players to soft muted list.");
    }

    public void resetSoftMutedPlayers() {
        softMutedPlayers = config.getStringList("soft-muted-players");
        GeneralUtils.pluginLogger(ChatColor.GREEN, "Removed overnight soft muted players from softmute list.");
    }
}
