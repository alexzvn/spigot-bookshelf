package io.mineverse.bungee;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

public class PlayerEventListener implements Listener {
    private Channel channel;

    public PlayerEventListener(Connection conn) {
        try {
            channel = conn.createChannel();
            channel.exchangeDeclare("game.player", "direct", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) throws IOException {
        channel.basicPublish(
            "game.player",
            "quit",
            null,
            e.getPlayer().getName().getBytes()
        );
    }
}
