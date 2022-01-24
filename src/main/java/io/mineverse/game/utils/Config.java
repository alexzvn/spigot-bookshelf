package io.mineverse.game.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static String getString(String path) {
        return config().getString(path);
    }

    public static String getString(String path, String def) {
        return config().getString(path, def);
    }

    public static int getInt(String path) {
        return config().getInt(path);
    }

    public static int getInt(String path, int def) {
        return config().getInt(path, def);
    }

    public static boolean getBoolean(String path) {
        return config().getBoolean(path);
    }

    public static boolean getBoolean(String path, boolean def) {
        return config().getBoolean(path, def);
    }

    public static double getDouble(String path) {
        return config().getDouble(path);
    }

    public static double getDouble(String path, double def) {
        return config().getDouble(path, def);
    }

    public static long getLong(String path) {
        return config().getLong(path);
    }

    public static long getLong(String path, long def) {
        return config().getLong(path, def);
    }

    public static FileConfiguration config() {
        return Instance.plugin().getConfig();
    }
}
