package io.mineverse.game.events;

import io.mineverse.game.foundation.Event;

public class PlayerWalletLinkedEvent extends Event {

    private String player;
    private String wallet;

    public PlayerWalletLinkedEvent(String player, String wallet) {
        this.player = player;
        this.wallet = wallet;
    }

    public String getPlayer() {
        return this.player;
    }

    public String getWallet() {
        return this.wallet;
    }
}
