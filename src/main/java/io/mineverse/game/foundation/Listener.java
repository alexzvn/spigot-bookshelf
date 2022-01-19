package io.mineverse.game.foundation;

import org.bukkit.scheduler.BukkitRunnable;

import io.mineverse.game.utils.Instance;
import io.socket.emitter.Emitter;

public abstract class Listener implements Emitter.Listener {
    public abstract void call(Object... args);

    protected void dispatch(Event event, String id) {

        BukkitRunnable task = new BukkitRunnable(){

            @Override
            public void run() {
                Instance.plugin().getEventRegister().dispatchConfirm(event, id);
            }
        };

        task.runTask(Instance.plugin());
    }
}
