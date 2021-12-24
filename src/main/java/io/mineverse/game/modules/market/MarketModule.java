package io.mineverse.game.modules.market;

import io.mineverse.game.events.PlayerWalletLinkedEvent;
import io.mineverse.game.foundation.SocketIO;
import io.mineverse.game.utils.Instance;

public class MarketModule extends SocketIO {
    public static void name() {
        Instance.dispatchEvent(new PlayerWalletLinkedEvent("player", "wallet"));
    }
}
