package io.mineverse.game.foundation.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Slot {
    public final int slot;

    public final Inventory inv;

    protected final List<ClickType> allows = new ArrayList<>();

    protected final List<ClickType> denies = new ArrayList<>();

    protected Optional<ClickConsumer> clickHandler = Optional.empty();

    public Slot(int slot, Inventory inv) {
        this.slot = slot;
        this.inv = inv;
    }

    public void onClick(ClickConsumer handler) {
        this.clickHandler = Optional.of(handler);
    }

    public ItemStack get() {
        return inv.getItem(slot);
    }

    public void set(ItemStack item) {
        inv.setItem(slot, item);
    }

    public void clearClickTypes() {
        allows.clear();
        denies.clear();
    }

    public void allow(ClickType... types) {
        for (ClickType type : types) {
            allows.add(type);
        }
    }

    public void deny(ClickType... types) {
        for (ClickType type : types) {
            denies.add(type);
        }
    }

    public void denyAll() {
        allows.clear();

        for (ClickType type : ClickType.values()) {
            denies.add(type);
        }
    }

    public void allowAll() {
        denies.clear();

        for (ClickType type : ClickType.values()) {
            allows.add(type);
        }
    }

    void click(InventoryEvent e) {
        if (! (e instanceof InventoryClickEvent)) {
           return;
        }

        InventoryClickEvent event = (InventoryClickEvent) e;

        if (!allows.contains(event.getClick()) || denies.contains(event.getClick())) {
            event.setCancelled(true);
            return;
        }

        clickHandler.ifPresent(handler -> handler.accept(event));
    }

    @FunctionalInterface
    public interface ClickConsumer {
        void accept(InventoryClickEvent type);
    }
}
