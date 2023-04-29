package com.jliii.primumnonnocere.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GeneralUtils {

    public static String pluginPrefix() {
        return "§f[§aPrimumNonNocere§f]§r ";
    }

    public static void pluginLogger(String message) {
        Bukkit.getConsoleSender().sendMessage(pluginPrefix() + message);
    }

    public static void pluginLogger(ChatColor chatColor, String message) {
        Bukkit.getConsoleSender().sendMessage(pluginPrefix() + chatColor + message);
    }

}
