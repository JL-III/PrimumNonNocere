package com.jliii.primumnonnocere.tasks;

import com.jliii.primumnonnocere.managers.ConfigManager;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DataSyncTask extends BukkitRunnable {

    private final ConfigManager configManager;
    ZoneId zoneId = ZoneId.of("America/Chicago"); // Central US time zone
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    LocalTime localTime = now.toLocalTime();

    LocalTime nightSync = LocalTime.of(23, 0); // 11 PM
    LocalTime morningSync = LocalTime.of(8, 30); // 8:30 AM

    public DataSyncTask(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void run() {
        overNightSync();
        GeneralUtils.pluginLogger("Syncing in game data with config.");
        configManager.saveConfig();
    }

    public void overNightSync() {
        // Check if the current time is within a small window around the target times (e.g., within 2.5 minutes)
        if (localTime.isAfter(nightSync.minusMinutes(4)) && localTime.isBefore(nightSync.plusMinutes(5))) {
            GeneralUtils.pluginLogger("Soft muting borderline players overnight.");
            configManager.addOverNightSoftMutedPlayers();
        }
        if (localTime.isAfter(morningSync.minusMinutes(4)) && localTime.isBefore(morningSync.plusMinutes(5))) {
            GeneralUtils.pluginLogger("Removing overnight soft muted players from softmute list.");
            configManager.resetSoftMutedPlayers();
        }
    }

}
