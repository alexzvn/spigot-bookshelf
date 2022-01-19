package io.mineverse.game.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.mineverse.game.foundation.Event;

public class PlayerWalletLinkedEvent extends Event {

    private Player player;
    private String wallet;

    public PlayerWalletLinkedEvent(String player_uuid, String wallet) {
        this.player = Bukkit.getPlayer(UUID.fromString(player_uuid));
        this.wallet = wallet;
    }

    public Player getPlayer() {
        return player;
    }

    public Boolean isPlayerOffline() {
        return player == null;
    }

    public String getWalletAddress() {
        return this.wallet;
    }
}
