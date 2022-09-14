package io.mineverse.game.meta.market;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import io.mineverse.game.utils.Json;

public class MarketItem implements JSONAware {
    /**
     * Sha1 hash of data
     */
    public final String hash;

    /**
     * Slot number index in inventory
     */
    public final Integer slot;

    /**
     * Base64 of encoded ItemStack
     */
    public final String data;

    /**
     * the value stand between 0 and 1
     */
    public final Float durability;

    public final String name;
    public final String[] lore;
    public final Integer amount;
    public final String type;
    public final String material;

    protected ItemStack item;

    public MarketItem(ItemStack item, String type, int slot) throws IOException {
        this.amount = item.getAmount();
        this.name = item.getItemMeta().getDisplayName();
        this.material = item.getType().name();
        this.type = type;
        this.slot = slot;
        this.durability = .99f;

        this.data = encode(item);
        this.hash = DigestUtils.sha1Hex(data);

        if (item.getItemMeta().hasLore()) {
            this.lore = item.getItemMeta().getLore().toArray(new String[0]);
        } else {
            this.lore = new String[0];
        }
    }

    public MarketItem(String hash, String data, String type, int slot, int amount, String name, String[] lore, String material, float durability) {
        this.hash = hash;
        this.data = data;
        this.type = type;
        this.slot = slot;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.durability = durability;
    }

    public MarketItem(ItemStack item, int slot) throws IOException {
        this(item, null, slot);
    }

    public ItemStack getItemStack() {
        if (item == null) {
            try {
                item = decode(data);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return item;
    }

    public String toJson() {
        return Json.stringify(this);
    }

    public static MarketItem fromJson(String data) throws IOException {
        JSONObject json = Json.parse(data);

        MarketItem item = new MarketItem(
            (String) json.get("hash"),
            (String) json.get("data"),
            (String) json.get("type"),
            ((Long) json.get("slot")).intValue(),
            ((Long) json.get("amount")).intValue(),
            (String) json.get("name"),
            ((String[]) json.get("lore")),
            (String) json.get("material"),
            ((Double) json.get("durability")).floatValue()
        );

        try {
            item.item = decode(item.data);
        } catch (ClassNotFoundException e) {
            // impossibble
            e.printStackTrace();
        }

        return item;
    }

    public static String encode(ItemStack item) throws IOException {
        ByteArrayOutputStream rawIO = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitIO = new BukkitObjectOutputStream(rawIO);

        bukkitIO.writeObject(item);
        bukkitIO.flush();

        byte[] buffer = rawIO.toByteArray();

        return Base64.getEncoder().encodeToString(buffer);
    }

    public static ItemStack decode(String data) throws IOException, ClassNotFoundException {
        byte[] buffer = Base64.getDecoder().decode(data);

        BukkitObjectInputStream bukkitIO = new BukkitObjectInputStream(new ByteArrayInputStream(buffer));

        return (ItemStack) bukkitIO.readObject();
    }

    public static String sha1(String data) {
        return DigestUtils.sha1Hex(data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toJSONString() {
        JSONObject data = new JSONObject();

        JSONArray lore = new JSONArray();

        for (String line : this.lore) {
            lore.add(line);
        }

        data.put("hash", hash);
        data.put("data", this.data);
        data.put("type", type);
        data.put("slot", slot);
        data.put("amount", amount);
        data.put("name", name);
        data.put("lore", lore);
        data.put("material", material);
        data.put("durability", durability);

        return data.toJSONString();
    }

    public static MarketItem from(JSONObject json) {
        try {
            ItemStack item = decode((String) json.get("data"));

            return new MarketItem(
                item,
                ((Long) json.get("slot")).intValue()
            );
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
