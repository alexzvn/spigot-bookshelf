package io.mineverse.game.utils;

import java.lang.reflect.Method;
import java.net.URI;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import io.mineverse.game.Bookshelf;
import io.mineverse.game.foundation.CancellableEvent;
import io.socket.client.IO;
import io.socket.client.Socket;

public class Instance {
    public static Socket createSocket(String url) {
        return IO.socket(URI.create(url));
    }

    public static void dispatchEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);

        if (! (event instanceof CancellableEvent)) {
            return;
        }

        CancellableEvent cancellable = (CancellableEvent) event;

        if (cancellable.isCancelled()) {
            return;
        }

        try {
            Method method = cancellable.getClass().getMethod("onApprove");

            method.setAccessible(true);

            method.invoke(event);
        }

        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin(), task);
    }

    public static void runDelay(Runnable task, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin(), task, delay);
    }

    public static java.util.logging.Logger logger() {
        return plugin().getLogger();
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin());
    }

    public static void bc(String message) {
        Bukkit.broadcastMessage(Message.color(message));
    }

    public static void tell(Entity entity, String message) {
        message = Message.color(Config.getString("message_prefix") + message);

        entity.sendMessage(message);
    }

    public static Bookshelf plugin() {
        return Bookshelf.getInstance();
    }

    public static NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(plugin(), key);
    }
}
