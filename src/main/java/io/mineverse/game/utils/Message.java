package io.mineverse.game.utils;

import org.bukkit.ChatColor;

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
}
