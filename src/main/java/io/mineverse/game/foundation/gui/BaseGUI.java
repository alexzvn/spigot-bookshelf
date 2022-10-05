package io.mineverse.game.foundation.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseGUI implements InventoryHolder {
    private Map<Integer, Slot> slots;

    protected Inventory inventory;

    Optional<OnOpenConsumer> onOpen = Optional.empty();

    Optional<OnCloseConsumer> onClose = Optional.empty();

    public BaseGUI() {
        reset();
    }

    /**
     * Define inventory and stuff
     */
    abstract public Inventory define();

    /**
     * Setup inventory after define
     * such as slot, mask, etc, ... 
     */
    abstract public void setup();

    final public void show(HumanEntity entity) {
        entity.openInventory(inventory);
    }

    final public void reset() {
        inventory = define();
        slots = new HashMap<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            Slot origin = new Slot(i, inventory);
            slots.put(i, origin);
        }

        setup();
    }

    public void onOpen(OnOpenConsumer consumer) {
        onOpen = Optional.of(consumer);
    }

    public void onClose(OnCloseConsumer consumer) {
        onClose = Optional.of(consumer);
    }

    void handleOpen(InventoryOpenEvent e) {
        onOpen.ifPresent(handler -> handler.accept(e));
    }

    void handleClose(InventoryCloseEvent e) {
        onClose.ifPresent(handler -> handler.accept(e));
    }

    void handleClick(InventoryClickEvent e) {
        if (e.getSlot() == e.getRawSlot()) {
            slots.get(e.getSlot()).click(e);
        }
    }

    final public Slot getSlot(int slot) {
        return slots.get(slot);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    final public static void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ListenerGUI(), plugin);
    }

    @FunctionalInterface
    public interface OnCloseConsumer {
        void accept(InventoryCloseEvent event);
    }

    @FunctionalInterface
    public interface OnOpenConsumer {
        void accept(InventoryOpenEvent event);
    }
}
