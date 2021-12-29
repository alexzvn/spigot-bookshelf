package io.mineverse.game.foundation;

import io.mineverse.game.utils.Instance;
import io.socket.emitter.Emitter;

public abstract class Listener implements Emitter.Listener {
    public abstract void call(Object... args);

    protected void dispatch(Event event, String id) {
        Instance.plugin().getEventRegister().dispatchConfirm(event, id);
    }
}
