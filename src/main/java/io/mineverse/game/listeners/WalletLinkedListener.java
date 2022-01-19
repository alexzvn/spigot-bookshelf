package io.mineverse.game.listeners;

import org.json.simple.JSONObject;

import io.mineverse.game.events.PlayerWalletLinkedEvent;
import io.mineverse.game.foundation.Event;
import io.mineverse.game.foundation.Listener;
import io.mineverse.game.utils.Json;

public class WalletLinkedListener extends Listener {

    @Override
    public void call(Object... args) {
        JSONObject object = Json.parse((String) args[0]);

        Event event = new PlayerWalletLinkedEvent(
            object.get("player_id").toString(),
            object.get("wallet").toString()
        );

        dispatch(event, object.get("id").toString());
    }
}
