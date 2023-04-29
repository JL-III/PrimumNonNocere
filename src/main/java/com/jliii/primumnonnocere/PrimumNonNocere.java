package com.jliii.primumnonnocere;

import com.jliii.primumnonnocere.commands.AdminCommands;
import com.jliii.primumnonnocere.listeners.VentureChatListener;
import com.jliii.primumnonnocere.managers.ConfigManager;
import com.jliii.primumnonnocere.tasks.DataSyncTask;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PrimumNonNocere extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigManager configManager = new ConfigManager(this);

        if (Bukkit.getPluginManager().getPlugin("VentureChat") != null) {
            GeneralUtils.pluginLogger(ChatColor.GREEN, "VentureChat found! Enabling Listener.");
            Bukkit.getPluginManager().registerEvents(new VentureChatListener(configManager), this);
            Objects.requireNonNull(Bukkit.getPluginCommand("softmute")).setExecutor(new AdminCommands(configManager));
            new DataSyncTask(configManager).runTaskTimer(this, 0, 20 * 60 * 5);
        } else {
            GeneralUtils.pluginLogger(ChatColor.RED, "VentureChat not found! Chat Listener will not work.");
            GeneralUtils.pluginLogger("Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
