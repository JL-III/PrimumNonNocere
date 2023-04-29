package com.jliii.primumnonnocere;

import com.jliii.primumnonnocere.commands.AdminCommands;
import com.jliii.primumnonnocere.listeners.VentureChatListener;
import com.jliii.primumnonnocere.managers.ConfigManager;
import com.jliii.primumnonnocere.tasks.DataSyncTask;
import com.jliii.primumnonnocere.tasks.OverNightTask;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PrimumNonNocere extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigManager configManager = new ConfigManager(this);

        if (Bukkit.getPluginManager().getPlugin("VentureChat") != null) {
            GeneralUtils.pluginLogger("VentureChat found! Enabling Listener.");
            Bukkit.getPluginManager().registerEvents(new VentureChatListener(configManager), this);
            return;
        } else {
            GeneralUtils.pluginLogger("VentureChat not found! Chat Listener will not work.");
        }

        Objects.requireNonNull(Bukkit.getPluginCommand("tchat")).setExecutor(new AdminCommands(configManager));
        new OverNightTask(configManager).runTaskTimer(this, 0, 20 * 60 * 60 * 24);
        new DataSyncTask(configManager).runTaskTimer(this, 0, 20 * 60 * 5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
