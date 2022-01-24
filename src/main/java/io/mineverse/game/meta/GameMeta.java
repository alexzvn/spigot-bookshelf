package io.mineverse.game.meta;

import io.mineverse.game.meta.listeners.*;
import io.mineverse.game.utils.Instance;

public class GameMeta {
    protected final MetaListener[] listeners = new MetaListener[] {
        new ItemCommandListener()
    };

    public void bind() {
        this.registerListeners();
    }

    protected void registerListeners() {
        for (MetaListener listener : listeners) {
            if (listener.shouldEnable()) {
                Instance.registerListener(listener);
            }
        }
    }
}
