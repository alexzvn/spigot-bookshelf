package io.mineverse.game;

import org.bukkit.plugin.java.JavaPlugin;

import io.mineverse.game.meta.GameMeta;
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

        BookshelfAPI.setInstance(new BookshelfAPI(this));
    }

    @Override
    public void onEnable() {
        bindSocket();

        new GameMeta().bind();
    }

    @Override
    public void onDisable() {
        BookshelfAPI.setInstance(null);
        socket.disconnect();
    }

    protected void bindSocket() {
        if (Config.getBoolean("bookshelf.enable") == false) return;

        socket = Instance.createSocket(Config.getString("bookshelf.socket"));

        eventRegister.bind(socket, Config.getString("bookshelf.token"));

        socket.connect();
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
