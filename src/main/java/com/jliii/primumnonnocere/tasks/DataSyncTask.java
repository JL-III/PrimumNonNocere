package com.jliii.primumnonnocere.tasks;

import com.jliii.primumnonnocere.managers.ConfigManager;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class DataSyncTask extends BukkitRunnable {

    private final ConfigManager configManager;

    public DataSyncTask(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void run() {
        GeneralUtils.pluginLogger("Syncing in game data with config.");
        configManager.saveConfig();
    }

}
