package com.jliii.primumnonnocere.utils;

import org.bukkit.Bukkit;

public class GeneralUtils {

    public static String pluginPrefix() {
        return "§f[§aPrimumNonNocere§f]§r ";
    }

    public static void pluginLogger(String message) {
        Bukkit.getConsoleSender().sendMessage(pluginPrefix() + message);
    }

}
