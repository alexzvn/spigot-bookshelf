package io.mineverse.bungee;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unused")
public class BungeeBookshelf extends Plugin {
    private Configuration config;

    private Connection connection;

    private static BungeeBookshelf instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        createRabbitConnection();
        instance = this;

        if (connection != null && connection.isOpen()) {
            getProxy().getPluginManager().registerListener(this, new PlayerEventListener(connection));
        }
    }

    @Override
    public void onDisable() {
        instance = null;

        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createRabbitConnection() {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(config.getString("rabbitmq.host"));
        factory.setPort(config.getInt("rabbitmq.port"));

        if (config.getBoolean("rabbitmq.use_auth")) {
            factory.setUsername(config.getString("rabbitmq.username"));
            factory.setPassword(config.getString("rabbitmq.password"));
        }

        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            getLogger().severe("RabbitMQ connection failed to initialize!!!");
            e.printStackTrace();
        }
    }

    private void saveDefaultConfig() {
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            getDataFolder().mkdirs();
            try {
                provider.save(provider.load(getResourceAsStream("config.yml")), configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadConfig() {
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            config = provider.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getRabbitConnection() {
        return connection;
    }

    public static BungeeBookshelf getInstance() {
        return instance;
    }
}
