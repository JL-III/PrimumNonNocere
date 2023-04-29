package com.jliii.primumnonnocere.listeners;

import com.jliii.primumnonnocere.managers.ConfigManager;
import mineverse.Aust1n46.chat.api.events.VentureChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

public class VentureChatListener implements Listener {

    private final ConfigManager configManager;

    public VentureChatListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    synchronized void onPlayerChat(VentureChatEvent event) {
        Player player = event.getMineverseChatPlayer().getPlayer();
        List<String> softMutedPlayers = configManager.getSoftMutedPlayers();
        if (softMutedPlayers.contains(player.getName().toLowerCase())) {
            event.getRecipients().clear();
            for (Player onlinePlayer: Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("theatriachat.softmute.listen") && !onlinePlayer.equals(player)) {
                    String softMuteMessage = ChatColor.RED + "|||" + ChatColor.RESET + event.getFormat() + ChatColor.GRAY + event.getChat();
                    onlinePlayer.sendMessage(softMuteMessage);
                }
            }
            event.getRecipients().add(player);
        } else if (configManager.containsBlacklistedWords(event.getChat(), configManager.getBlackListedWords(), 1)) {
            event.getRecipients().clear();
            for (Player onlinePlayer: Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("theatriachat.softmute.listen") && !onlinePlayer.equals(player)) {
                    String blacklistMessage = ChatColor.RED + "|||" + ChatColor.RESET + event.getFormat() + ChatColor.GRAY + event.getChat();
                    onlinePlayer.sendMessage(blacklistMessage);
                }
            }
            event.getRecipients().add(player);
        }
    }
}
