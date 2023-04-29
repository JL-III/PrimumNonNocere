package com.jliii.primumnonnocere.managers;

import com.jliii.primumnonnocere.PrimumNonNocere;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigManager {

    PrimumNonNocere plugin;
    FileConfiguration config;
    List<String> softMutedPlayers;
    List<String> overNightSoftMutedPlayers;

    public ConfigManager(PrimumNonNocere plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        this.softMutedPlayers = config.getStringList("soft-muted-players");
        this.overNightSoftMutedPlayers = config.getStringList("overnight-soft-mutes");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        softMutedPlayers = config.getStringList("soft-muted-players");
        overNightSoftMutedPlayers = config.getStringList("overnight-soft-mutes");
    }

    public void saveConfig() {
        config.set("soft-muted-players", softMutedPlayers);
        config.set("overnight-soft-mutes", overNightSoftMutedPlayers);
        plugin.saveConfig();
    }


    public List<String> getSoftMutedPlayers() {
        return softMutedPlayers;
    }

    public List<String> getOverNightSoftMutedPlayers() {
        return overNightSoftMutedPlayers;
    }

    public Set<String> compareMemoryAndConfig() {
        return getDifferences(softMutedPlayers, config.getStringList("soft-muted-players"));
    }

    public static Set<String> getDifferences(List<String> list1, List<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        Set<String> differences = new HashSet<>(set1);
        differences.removeAll(set2);
        set2.removeAll(set1);
        differences.addAll(set2);
        return differences;
    }

    public void addOverNightSoftMutedPlayers() {
        List<String> newPlayers = getOverNightSoftMutedPlayers().stream()
                .filter(player -> !softMutedPlayers.contains(player)).collect(Collectors.toList());
        softMutedPlayers.addAll(newPlayers);
        GeneralUtils.pluginLogger(ChatColor.GREEN, "Added " + newPlayers.size() + " players to soft muted list.");
        newPlayers.forEach(GeneralUtils::pluginLogger);
        saveConfig();
    }

    public void resetSoftMutedPlayers() {
        softMutedPlayers = getSoftMutedPlayers().stream()
                .filter(player -> !overNightSoftMutedPlayers.contains(player)).collect(Collectors.toList());
        GeneralUtils.pluginLogger(ChatColor.GREEN, "Removing over night soft mutes, if any.");
        softMutedPlayers.forEach(GeneralUtils::pluginLogger);
        saveConfig();
    }
}
