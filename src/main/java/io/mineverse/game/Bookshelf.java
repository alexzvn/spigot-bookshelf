package io.mineverse.game;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.bukkit.plugin.java.JavaPlugin;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import io.mineverse.game.meta.GameMeta;
import io.mineverse.game.utils.Config;
import io.mineverse.game.utils.Instance;
import io.mineverse.game.utils.Log;
import io.socket.client.Socket;

public class Bookshelf extends JavaPlugin {

    protected static Bookshelf instance;

    protected Socket socket;

    protected EventRegister eventRegister;

    protected Connection rabbitConnection;

    protected GameMeta gameMeta;

    public Bookshelf() {
        this.saveDefaultConfig();

        instance = this;
        eventRegister = new EventRegister();

        BookshelfAPI.setInstance(new BookshelfAPI(this));
    }

    @Override
    public void onEnable() {
        bindSocket();

        try {
            rabbitConnection = createRabbitConnection();
        } catch (Exception e) {
            Log.error("RabbitMQ connection failed to initialize!!!");
            Log.error(e.getMessage());
        }

        gameMeta = new GameMeta();

        gameMeta.bind();

        Log.info("Bookshelf has been enabled!");
    }

    @Override
    public void onDisable() {
        BookshelfAPI.setInstance(null);

        if (socket != null) {
            socket.disconnect();
        }

        if (rabbitConnection != null) {
            try {
                rabbitConnection.close();
            } catch (IOException e) {
                Log.error(e.getMessage());
            }
        }

        rabbitConnection = null;
    }

    protected void bindSocket() {
        if (Config.getBoolean("bookshelf.enable") == false) return;

        socket = Instance.createSocket(Config.getString("bookshelf.socket"));

        eventRegister.bind(socket, Config.getString("bookshelf.token"));

        socket.connect();
    }

    protected Connection createRabbitConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(Config.getString("rabbitmq.host"));
        factory.setPort(Config.getInt("rabbitmq.port"));

        if (Config.getBoolean("rabbitmq.use_auth")) {
            factory.setUsername(Config.getString("rabbitmq.username"));
            factory.setPassword(Config.getString("rabbitmq.password"));
        }

        return factory.newConnection();
    }

    public Connection getRabbitConnection() {
        return rabbitConnection;
    }

    public GameMeta getGameMeta() {
        return gameMeta;
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
