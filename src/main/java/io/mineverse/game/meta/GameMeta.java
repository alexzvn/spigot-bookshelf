package io.mineverse.game.meta;

import io.mineverse.game.foundation.SimpleAPI;
import io.mineverse.game.meta.commands.InventoryCommand;
import io.mineverse.game.meta.listeners.*;
import io.mineverse.game.meta.market.MarketInventoryManager;
import io.mineverse.game.utils.Config;
import io.mineverse.game.utils.Instance;

public class GameMeta {
    protected final MarketInventoryManager manager;

    public GameMeta() {
        SimpleAPI api = new SimpleAPI(
            Config.getString("market.endpoint"),
            Config.getString("market.token")
        );

        manager =  new MarketInventoryManager(Instance.plugin().getRabbitConnection(), api);
    }

    public MarketInventoryManager getMarketManager() {
        return manager;
    }

    protected final MetaListener[] listeners = new MetaListener[] {
        new ItemCommandListener()
    };

    public void bind() {
        this.registerListeners();
        this.registerCommands();
    }

    protected void registerListeners() {
        for (MetaListener listener : listeners) {
            if (listener.shouldEnable()) {
                Instance.registerListener(listener);
            }
        }
    }

    protected void registerCommands() {
        Instance.plugin().getCommandService().get("bookshelf").registerSub(new InventoryCommand());
    }
}
