package io.mineverse.game.listeners;

import org.json.simple.JSONObject;

import io.mineverse.game.events.PlayerWalletLinkedEvent;
import io.mineverse.game.foundation.Event;
import io.mineverse.game.foundation.Listener;
import io.mineverse.game.utils.Json;

public class WalletLinkedListener extends Listener {

    @Override
    public void call(Object... args) {
        JSONObject object = Json.parse(args[0].toString());

        JSONObject payload = (JSONObject) object.get("payload");

        Event event = new PlayerWalletLinkedEvent(
            (String) payload.get("player_uuid"),
            (String) payload.get("wallet")
        );

        dispatch(event, (String) object.get("_id"));
    }
}
