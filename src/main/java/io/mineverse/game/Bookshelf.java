package io.mineverse.game;

import org.bukkit.plugin.java.JavaPlugin;

public class Bookshelf extends JavaPlugin {

    protected static Bookshelf instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub
    }

    public static Bookshelf getInstance() {
        return instance;
    }
}
