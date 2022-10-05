package io.mineverse.game.foundation.gui.mask;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.mineverse.game.foundation.gui.BaseGUI;
import io.mineverse.game.foundation.gui.Mask;
import io.mineverse.game.foundation.gui.Slot;

public class BinaryMask extends Mask {
    protected ItemStack item = new ItemStack(Material.GLASS_PANE);

    public BinaryMask(BaseGUI gui) {
        super(gui);
    }

    public BinaryMask item(ItemStack item) {
        this.item = item;
        return this;
    }

    protected void setupSlot(char type, Slot slot) {
        if (type == '1') {
            slot.set(item);
            slot.denyAll();
        }

        if (type == '0') {
            slot.clearClickTypes();
        }
    }

    @Override
    public void apply() {
        for (int row = 0; row < patterns.size(); row++) {
            String pattern = patterns.get(row);

            for (int col = 0; col < pattern.length(); col++) {
                setupSlot(pattern.charAt(col), gui.getSlot(row * 9 + col));
            }
        }
    }
}
