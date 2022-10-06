package io.mineverse.game.foundation.gui;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

class ListenerGUI implements Listener {


    @EventHandler
    public void click(InventoryClickEvent e) {
        get(e).ifPresent(gui -> gui.handleClick(e));
    }

    @EventHandler
    public void open(InventoryOpenEvent e) {
        get(e).ifPresent(gui -> gui.handleOpen(e));
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        get(e).ifPresent(gui -> gui.handleClose(e));
    }

    protected Optional<BaseGUI> get(InventoryEvent e) {
        if (! (e.getInventory().getHolder() instanceof BaseGUI)) {
            return Optional.empty();
        }

        return Optional.of(
            (BaseGUI) e.getInventory().getHolder()
        );
    }
}
