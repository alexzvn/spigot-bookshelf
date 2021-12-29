package io.mineverse.game.utils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.common.io.Files;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util {

    /**
     * Create folder if not exists in current plugin data folder
     * 
     * @param name
     */
    public static void mkdir(String name) {
        File file = new File(Instance.plugin().getDataFolder().getPath() + '/' + name);

        if (file.exists() == false) {
            file.mkdirs();
        }
    }

    public static File file(String name) {
        return new File(Instance.plugin().getDataFolder().getPath() + '/' + name);
    }

    public static String readFile(File file) {
        String content = "";

        try {
            List<String> list = Files.readLines(file, StandardCharsets.UTF_8);

            for (String text : list) {
                content += text;
            }

            return content;
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeFile(File file, String content) {
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");

            writer.write(content);

            writer.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(File file, String content) {
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");

            writer.append(content);

            writer.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAirItem(ItemStack item) {
        if (item == null) {
            return true;
        }

        return Material.AIR.equals(item.getType());
    }

    public static boolean containItems(ItemStack[] items) {
        for (ItemStack item : items) {
            if (! isAirItem(item)) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack airItem() {
        return new ItemStack(Material.AIR);
    }

    public static Player humanToPlayer(HumanEntity human) {
        return Bukkit.getPlayer(human.getName());
    }

    public static void dropItem(ItemStack item, Location location) {
        if (isAirItem(item)) return;

        location.getWorld().dropItemNaturally(location, item);
    }

    public static void sendPlayerItem(ItemStack item, Player player) {
        if (isAirItem(item)) return;

        Inventory inv = player.getInventory();

        if (inv.firstEmpty() != -1) {
            inv.addItem(item); return;
        }

        dropItem(item, player.getLocation());
    }
}
