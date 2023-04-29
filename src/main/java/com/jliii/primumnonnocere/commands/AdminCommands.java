package com.jliii.primumnonnocere.commands;

import com.jliii.primumnonnocere.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminCommands implements CommandExecutor {

    private final ConfigManager configManager;

    public AdminCommands(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Bukkit.getLogger().info("Only players can use this command.");
            return true;
        }

        if (args.length == 1) {
            if (player.hasPermission("softmute.admin")) {
                String action = args[0].toLowerCase();
                switch (action) {
                    case "reload" -> {
                        configManager.reloadConfig();
                        player.sendMessage("Reloading config...");
                    }
                    case "save" -> {
                        configManager.saveConfig();
                        player.sendMessage("Saving config...");
                        player.sendMessage(ChatColor.GREEN + "Players currently soft muted:");
                        configManager.getSoftMutedPlayers().forEach(player::sendMessage);
                    }
                    case "list" -> {
                        player.sendMessage(ChatColor.GREEN + "Players currently soft muted:");
                        configManager.getSoftMutedPlayers().forEach(player::sendMessage);
                    }
                    case "ns" -> configManager.addOverNightSoftMutedPlayers();
                    case "rs" -> configManager.resetSoftMutedPlayers();
                }
            }
            return true;
        }



        if (player.hasPermission("softmute.others") && args.length == 2 && args[0].equals("toggle")) {
            String playerName = args[1].toLowerCase();
            List<String> softMutedPlayers = configManager.getSoftMutedPlayers();

            if (!softMutedPlayers.contains(playerName)) {
                softMutedPlayers.add(playerName);
                player.sendMessage("Soft muted " + args[1]);
            } else {
                softMutedPlayers.remove(playerName);
                player.sendMessage("Un-soft muted " + args[1]);
            }
        }

        return true;
    }
}
