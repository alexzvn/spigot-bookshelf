package io.mineverse.game.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import io.mineverse.game.Bookshelf;
import io.mineverse.game.foundation.CancellableEvent;

public class Instance {


    public static void dispatchEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);

        if (! (event instanceof CancellableEvent)) {
            return;
        }

        CancellableEvent cancellable = (CancellableEvent) event;

        if (cancellable.isCancelled() == false) {
            cancellable.finish();
        }
    }

    public static void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin(), task);
    }

    public static void runDelay(Runnable task, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin(), task, delay);
    }

    public static Bookshelf plugin() {
        return Bookshelf.getInstance();
    }
}
