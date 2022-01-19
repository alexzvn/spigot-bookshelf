package io.mineverse.game.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.mineverse.game.foundation.Event;
import io.mineverse.game.utils.Util;

public class PlayerWalletLinkedEvent extends Event {

    private String player;
    private String wallet;

    public PlayerWalletLinkedEvent(String player_uuid, String wallet) {
        this.player = player_uuid;
        this.wallet = wallet;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(Util.dashedUUID(player));
    }

    public Boolean isPlayerOffline() {
        return getPlayer().isOnline() == false;
    }

    public String getWalletAddress() {
        return this.wallet;
    }
}
