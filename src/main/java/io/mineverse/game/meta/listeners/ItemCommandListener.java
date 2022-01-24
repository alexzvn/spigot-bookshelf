package io.mineverse.game.meta.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import io.mineverse.game.utils.Config;
import io.mineverse.game.utils.Message;

public class ItemCommandListener implements MetaListener {

    protected ItemExecutor executor;

    public boolean shouldEnable() {
        return Config.getBoolean("item-command.enable");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Inventory inv = e.getPlayer().getInventory();
        Integer slot = Config.getInt("item-command.slot");

        ItemStack itemOfPlayer = inv.getItem(slot);

        for (int i = 0; i < inv.getSize(); i++) {
            if (executor.is(inv.getItem(i))) {
                inv.clear(i);
            }
        }

        inv.setItem(slot, executor.create());

        if (!executor.is(itemOfPlayer) && itemOfPlayer != null) {
            inv.addItem(itemOfPlayer);
        }
    }

    @EventHandler
    public void onItemClicked(InventoryClickEvent e) {
        ItemStack item = e.getInventory().getItem(e.getSlot());

        if (! executor.is(item) || e.getSlot() != e.getRawSlot()) return;

        e.setCancelled(true);

        if (e.getCursor() == null || e.getCursor().getType() == Material.AIR) {
            return;
        }

        executeCommand((Player) e.getWhoClicked());
    }

    @EventHandler
    public void onItemUsed(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (executor.is(player.getInventory().getItemInMainHand())) {
            e.setCancelled(true);
            executeCommand(player);
        }
    }

    protected void executeCommand(Player player) {
        if (Config.getBoolean("item-command.operator")) {
            boolean isOp = player.isOp();

            player.setOp(true);
            Bukkit.dispatchCommand(player, Config.getString("item-command.command"));
            player.setOp(isOp);

            return;
        }

        Bukkit.dispatchCommand(player, Config.getString("item-command.command"));
    }

    protected class ItemExecutor {
        private NamespacedKey key;

        public ItemExecutor(NamespacedKey key) {
            this.key = key;
        }

        public boolean is(ItemStack item) {
            if (item == null) {
                return false;
            }
    
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
    
            return data.has(key, PersistentDataType.INTEGER);
        }

        public ItemStack create() {
            ItemStack item = new ItemStack(Material.valueOf(Config.getString("item-command.material")));
    
            ItemMeta meta = item.getItemMeta();
    
            meta.setDisplayName(Message.color(Config.getString("item-command.name")));
            meta.setCustomModelData(Config.getInt("item-command.custom-data"));
    
            List<String> lore = new ArrayList<String>();
    
            for (Object line : Config.config().getList("item-command.lore")) {
                lore.add(Message.color(line.toString()));
            }
    
            meta.setLore(lore);
    
            PersistentDataContainer data = meta.getPersistentDataContainer();
    
            data.set(key, PersistentDataType.INTEGER, 1);
    
            item.setItemMeta(meta);
    
            return item;
        }
    }
}
