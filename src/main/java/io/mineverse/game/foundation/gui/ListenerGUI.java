package io.mineverse.game.foundation.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

class ListenerGUI implements Listener {


    @EventHandler
    public void listen(InventoryEvent e) {
        if (! (e.getInventory().getHolder() instanceof BaseGUI)) return;

        BaseGUI gui = (BaseGUI) e.getInventory().getHolder();

        if (e instanceof InventoryClickEvent) {
            gui.handleClick((InventoryClickEvent) e);
        }

        if (e instanceof InventoryOpenEvent) {
            gui.handleOpen((InventoryOpenEvent) e);
        }

        if (e instanceof InventoryCloseEvent) {
            gui.handleClose((InventoryCloseEvent) e);
        }
    }
}
