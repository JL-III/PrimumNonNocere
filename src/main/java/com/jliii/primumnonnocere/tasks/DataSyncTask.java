package com.jliii.primumnonnocere.tasks;

import com.jliii.primumnonnocere.managers.ConfigManager;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class DataSyncTask extends BukkitRunnable {

    private final ConfigManager configManager;
    ZoneId zoneId = ZoneId.of("America/Chicago"); // Central US time zone

    LocalTime nightSync = LocalTime.of(23, 0); // 11 PM
    LocalTime morningSync = LocalTime.of(8, 30); // 8:30 AM

    public DataSyncTask(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void run() {
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        LocalTime localTime = now.toLocalTime();
        if (!overNightSync(localTime)) {
            sync();
        }
    }

    public void sync() {
        GeneralUtils.pluginLogger(ChatColor.LIGHT_PURPLE, "Checking in game data with config.");
        configManager.getSoftMutedPlayers().forEach(player -> {
            GeneralUtils.pluginLogger(player + ChatColor.DARK_RED + " -> soft muted.");
        });
        configManager.getOverNightSoftMutedPlayers().forEach(player -> {
            GeneralUtils.pluginLogger(player + ChatColor.RED + " -> overnight soft muted.");
        });
        Set<String> differences = configManager.compareMemoryAndConfig();
        if (!differences.isEmpty()) {
            GeneralUtils.pluginLogger(ChatColor.GREEN, "Changes detected:");
            differences.forEach(player -> {
                GeneralUtils.pluginLogger(player + ChatColor.GOLD + " -> updating in config.");
            });
            configManager.saveConfig();
            GeneralUtils.pluginLogger(ChatColor.GREEN, "Config saved.");
        } else {
            GeneralUtils.pluginLogger(ChatColor.YELLOW, "No changes detected.");
        }
    }

    public boolean overNightSync(LocalTime localTime) {
        Duration nightDuration = Duration.between(localTime, nightSync).abs();
        Duration morningDuration = Duration.between(localTime, morningSync).abs();

        if (nightDuration.compareTo(Duration.ofMinutes(3)) <= 0) {
            GeneralUtils.pluginLogger("Checking if players need to be added to soft mute list for the night...");
            configManager.addOverNightSoftMutedPlayers();
            return true;
        } else if (morningDuration.compareTo(Duration.ofMinutes(3)) <= 0) {
            GeneralUtils.pluginLogger("Checking if players need to be removed from soft mute list for the morning...");
            configManager.resetSoftMutedPlayers();
            return true;
        }
        return false;
    }

}
