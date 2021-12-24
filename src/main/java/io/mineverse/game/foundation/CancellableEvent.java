package io.mineverse.game.foundation;

import org.bukkit.event.Cancellable;

public abstract class CancellableEvent extends Event implements Cancellable {
    protected boolean cancelled = false;

    /**
     * Call the logic of event if it is not cancelled.
     */
    abstract protected void onApprove();

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
