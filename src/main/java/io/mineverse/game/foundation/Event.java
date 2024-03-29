package io.mineverse.game.foundation;

import org.bukkit.event.HandlerList;

public abstract class Event extends org.bukkit.event.Event {

    protected static HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
