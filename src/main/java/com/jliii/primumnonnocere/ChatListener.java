package com.jliii.primumnonnocere;

import mineverse.Aust1n46.chat.api.events.VentureChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

public class ChatListener implements Listener {

    private final ConfigManager configManager;

    public ChatListener(ConfigManager configManager) {
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
        }
    }
}
