package io.mineverse.game.foundation.gui.mask;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import io.mineverse.game.foundation.gui.BaseGUI;
import io.mineverse.game.foundation.gui.Slot;

public class CustomCharMask extends BinaryMask {
    protected Map<Character, ItemStack> items = new HashMap<>();

    public CustomCharMask(BaseGUI gui) {
        super(gui);
    }

    public BinaryMask set(char type, ItemStack item) {
        items.put(type, item);
        return this;
    }

    public void unset(char type) {
        items.remove(type);
    }

    @Override
    protected void setupSlot(char type, Slot slot) {
        if (type == '0') {
            slot.allowAll();
            return;
        }

        if (! items.containsKey(type)) {
            throw new IllegalArgumentException("No item defined for character: " + type);
        }

        slot.set(items.get(type));
        slot.denyAll();
    }
}
