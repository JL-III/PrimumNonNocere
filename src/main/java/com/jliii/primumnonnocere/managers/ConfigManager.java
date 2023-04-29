package com.jliii.primumnonnocere.managers;

import com.jliii.primumnonnocere.PrimumNonNocere;
import com.jliii.primumnonnocere.utils.GeneralUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigManager {

    PrimumNonNocere plugin;
    FileConfiguration config;
    List<String> softMutedPlayers;
    List<String> overNightSoftMutedPlayers;
    List<String> blackListedWords;

    public ConfigManager(PrimumNonNocere plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        this.softMutedPlayers = config.getStringList("soft-muted-players");
        this.overNightSoftMutedPlayers = config.getStringList("overnight-soft-mutes");
        this.blackListedWords = config.getStringList("blacklisted-words");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        softMutedPlayers = config.getStringList("soft-muted-players");
        overNightSoftMutedPlayers = config.getStringList("overnight-soft-mutes");
        blackListedWords = config.getStringList("blacklisted-words");
    }

    public void saveConfig() {
        config.set("soft-muted-players", softMutedPlayers);
        config.set("overnight-soft-mutes", overNightSoftMutedPlayers);
        plugin.saveConfig();
    }


    public List<String> getSoftMutedPlayers() {
        return softMutedPlayers;
    }

    public List<String> getOverNightSoftMutedPlayers() {
        return overNightSoftMutedPlayers;
    }

    public List<String> getBlackListedWords() {
        return blackListedWords;
    }

    public boolean containsSimilarBlacklistedWords(String message, List<String> blacklistedWords, int maxAllowedDistance) {
        String[] words = message.split("\\s+");

        for (String word : words) {
            for (String blacklistedWord : blacklistedWords) {
                int distance = levenshteinDistance(word.toLowerCase(), blacklistedWord.toLowerCase());
                if (distance <= maxAllowedDistance && wordMatchesBoundary(message, word)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsBlacklistedWords(String message, List<String> blacklistedWords, int maxAllowedDistance) {
        String[] words = message.split("\\s+");

        for (String word : words) {
            for (String blacklistedWord : blacklistedWords) {
                if (wordMatchesBoundary(message, blacklistedWord)) {
                    // Exact blacklisted word found
                    return true;
                }

                int distance = levenshteinDistance(word.toLowerCase(), blacklistedWord.toLowerCase());
                if (distance <= maxAllowedDistance) {
                    // Misspelled blacklisted word found
                    return true;
                }

                if (word.toLowerCase().contains(blacklistedWord.toLowerCase())) {
                    // Blacklisted word attached to another allowed word found
                    return true;
                }
            }
        }

        return false;
    }

    public static int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

        for (int i = 0; i <= lhs.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 1; j <= rhs.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= lhs.length(); i++) {
            for (int j = 1; j <= rhs.length(); j++) {
                int cost = lhs.charAt(i - 1) == rhs.charAt(j - 1) ? 0 : 1;
                distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1), distance[i - 1][j - 1] + cost);
            }
        }
        return distance[lhs.length()][rhs.length()];
    }

    public boolean wordMatchesBoundary(String message, String word) {
        String patternString = "\\b" + word + "\\b";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(message);

        return matcher.find();
    }

    public Set<String> compareMemoryAndConfig() {
        return getDifferences(softMutedPlayers, config.getStringList("soft-muted-players"));
    }

    public static Set<String> getDifferences(List<String> list1, List<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        Set<String> differences = new HashSet<>(set1);
        differences.removeAll(set2);
        set2.removeAll(set1);
        differences.addAll(set2);
        return differences;
    }

    public void addOverNightSoftMutedPlayers() {
        List<String> newPlayers = getOverNightSoftMutedPlayers().stream()
                .filter(player -> !softMutedPlayers.contains(player)).collect(Collectors.toList());
        softMutedPlayers.addAll(newPlayers);
        GeneralUtils.pluginLogger(ChatColor.GREEN, "Added " + newPlayers.size() + " players to soft muted list.");
        newPlayers.forEach(GeneralUtils::pluginLogger);
        saveConfig();
    }

    public void resetSoftMutedPlayers() {
        softMutedPlayers = getSoftMutedPlayers().stream()
                .filter(player -> !overNightSoftMutedPlayers.contains(player)).collect(Collectors.toList());
        GeneralUtils.pluginLogger(ChatColor.GREEN, "Removing over night soft mutes, if any.");
        softMutedPlayers.forEach(GeneralUtils::pluginLogger);
        saveConfig();
    }


}
