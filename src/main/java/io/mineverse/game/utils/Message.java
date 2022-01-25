package io.mineverse.game.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class Message {

    public static String color(String color, char altColorChar) {
        return ChatColor.translateAlternateColorCodes(altColorChar, color);
    }

    public static String prefixColor(String msg) {
        return color(Config.getString("prefix") + msg);
    }

    public static String color(String color) {
        return color(color, '&');
    }

    public static String placeholder(String text, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }

        return text;
    }
}
