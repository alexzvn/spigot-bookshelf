package io.mineverse.game;

import org.bukkit.plugin.java.JavaPlugin;

import io.mineverse.game.utils.Config;
import io.mineverse.game.utils.Instance;
import io.socket.client.Socket;

public class Bookshelf extends JavaPlugin {

    protected static Bookshelf instance;

    protected Socket socket;

    protected EventRegister eventRegister;

    public Bookshelf() {
        this.saveDefaultConfig();

        instance = this;

        eventRegister = new EventRegister();
    }

    @Override
    public void onEnable() {
        socket = Instance.createSocket(Config.getString("bookshelf.socket"));

        eventRegister.bind(socket, Config.getString("bookshelf.token"));

        socket.connect();
    }

    @Override
    public void onDisable() {
        try {
            socket.disconnect();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public EventRegister getEventRegister() {
        return eventRegister;
    }

    public Socket getBookshelfSocket() {
        return socket;
    }

    public static Bookshelf getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Bookshelf instance is null!");
        }

        return instance;
    }
}
