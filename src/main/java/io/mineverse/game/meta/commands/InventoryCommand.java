package io.mineverse.game.meta.commands;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;

import io.mineverse.game.meta.market.MarketInventoryManager;
import io.mineverse.game.meta.market.MarketItem;
import io.mineverse.game.utils.Instance;

public class InventoryCommand implements Listener {

    public InventoryCommand() {
        Instance.registerListener(this);
    }

    @Command(name = "inventory", aliases = {}, desc = "Open your inventory", usage = "")
    public void inventory(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        MarketInventoryManager manager = Instance.plugin().getGameMeta().getMarketManager();

        List<MarketItem> items = manager.getInventoryItems(player);

        if (items == null) {
            player.sendMessage("An error occurred while fetching your inventory.");
            return;
        }

        InventoryHolder holder = new MarketInventoryHolder(player, items);

        player.openInventory(holder.getInventory());
    }

    @EventHandler
    public void saveInventoryOnClose(InventoryCloseEvent e) {

        if (! (e.getInventory().getHolder() instanceof MarketInventoryHolder)) {
            return;
        }

        MarketInventoryHolder holder = (MarketInventoryHolder) e.getInventory().getHolder();
        MarketInventoryManager manager = Instance.plugin().getGameMeta().getMarketManager();
        Player player = holder.getOwner();

        List<MarketItem> items = MarketInventoryManager.convertInventory(holder.getInventory());

        manager.saveInventoryItems(player, items);
    }

    class MarketInventoryHolder implements InventoryHolder {
        public final String id = NanoIdUtils.randomNanoId();

        private final Inventory inv = Bukkit.createInventory(this, 54, "Your Inventory");

        protected Player owner;

        public MarketInventoryHolder(Player player, List<MarketItem> items) {
            for (MarketItem item : items) {
                inv.setItem(item.slot, item.getItemStack());
            }

            owner = player;
        }

        public Player getOwner() {
            return owner;
        }

        @Override
        public Inventory getInventory() {
            return inv;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MarketInventoryHolder) {
                return ((MarketInventoryHolder) obj).id.equals(id);
            }
            return false;
        }
    }
}
