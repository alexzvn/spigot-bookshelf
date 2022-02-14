package io.mineverse.game.meta.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import io.mineverse.game.utils.Config;
import io.mineverse.game.utils.Instance;
import io.mineverse.game.utils.Message;
import io.mineverse.game.utils.Util;

public class ItemCommandListener implements MetaListener {

    protected ItemExecutor executor = new ItemExecutor(Instance.getNamespacedKey("item-command"));

    public boolean shouldEnable() {
        return Config.getBoolean("item-command.enable");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        setup(e.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        setup(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemClicked(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        if (! executor.is(item)) return;

        e.setCancelled(true);

        if (! Util.isAirItem(e.getCursor()) || player.getGameMode() == GameMode.CREATIVE) {
            e.setCancelled(false);
            return;
        }

        if (Util.isAirItem(e.getCursor())) {
            player.closeInventory();
            executeCommand(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void blockDrops(PlayerDeathEvent e) {
        Iterator<ItemStack> iterator =  e.getDrops().iterator();

        while (iterator.hasNext()) {
            ItemStack next = iterator.next();
            if (executor.is(next)) iterator.remove();
        }
    }

    @EventHandler
    public void onThrowawayItem(PlayerDropItemEvent e) {
        if (executor.is(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onUseItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (executor.is(player.getInventory().getItemInMainHand())) {
            e.setCancelled(true);
            executeCommand(player);
        }
    }

    protected void setup(Player p) {
        Inventory inv = p.getInventory();
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

    protected void executeCommand(Player player) {
        String command = Message.placeholder(Config.getString("item-command.command"), player);

        switch (Config.getString("item-command.run-as")) {
            case "console": Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command); break;
            case "operator": runCommandAsOperator(player, command); break;
            default: Bukkit.dispatchCommand(player, command); break;
        }
    }

    private void runCommandAsOperator(Player player, String command) {
        boolean isOp = player.isOp();

        try {
            player.setOp(true);
            Bukkit.dispatchCommand(player, command);
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        player.setOp(isOp);
    }

    protected class ItemExecutor {
        private NamespacedKey key;

        public ItemExecutor(NamespacedKey key) {
            this.key = key;
        }

        public boolean is(ItemStack item) {
            if (item == null || item.getItemMeta() == null) {
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
