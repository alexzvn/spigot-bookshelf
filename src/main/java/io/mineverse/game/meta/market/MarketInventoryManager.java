package io.mineverse.game.meta.market;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import io.mineverse.game.foundation.SimpleAPI;
import io.mineverse.game.utils.Json;
import io.mineverse.game.utils.Log;
import io.mineverse.game.utils.Util;

public class MarketInventoryManager {

    protected final Connection rabbit;
    protected final SimpleAPI api;

    protected final String QUEUE_NAME = "universal:inventory";

    protected Channel channel;

    protected boolean marketPing = false;

    public MarketInventoryManager(Connection rabbit, SimpleAPI api) {
        this.rabbit = rabbit;
        this.api = api;

        init();
    }

    private void init() {
        if (rabbit == null || !rabbit.isOpen()) {
            return;
        }

        try {
            channel = rabbit.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MarketItem> getInventoryItems(Player player) {
        try {
            Response res = api.get("/player/" + player.getUniqueId().toString() + "/inventory").execute();

            return decodeInventory(res.returnContent().asString());

        } catch (IOException e) {
            marketPing = false;
        }

        return null;
    }

    public boolean saveInventoryItems(Player player, List<MarketItem> items) {
        return syncInventory(player, items);
    }

    protected boolean syncInventory(Player player,  List<MarketItem> items) {
        String payload = JSONArray.toJSONString(items);
        String uuid = player.getUniqueId().toString();

        if (syncViaAPI(uuid, payload)) {
            return true; // oke
        }

        Log.warning("Failed to sync inventory via API, trying RabbitMQ");

        HashMap<String, Object> data = new HashMap<>();

        data.put("uuid", uuid);
        data.put("payload", payload);

        payload = JSONObject.toJSONString(data);

        if (! syncViaRabbit(payload)) {
            Log.error("Failed to sync inventory via RabbitMQ");

            return false;
        }

        return true;
    }

    protected boolean syncViaAPI(String uuid, String body) {
        Request req = api.put("/player/" + uuid + "/inventory")
            .addHeader("accept", "application/json")
            .bodyString(body, ContentType.APPLICATION_JSON);

        try {
            Response res = req.execute();

            if (res.returnResponse().getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    protected boolean syncViaRabbit(String message) {
        try {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static List<MarketItem> convertInventory(Inventory inv) {
        List<MarketItem> items = new ArrayList<>();

        for (int i = 0; i < inv.getSize(); i++) {
            final ItemStack item = inv.getItem(i);

            if (Util.isAirItem(item)) continue;

            try {
                items.add(new MarketItem(item, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return items;
    }

    protected static List<MarketItem> decodeInventory(String response) {
        List<MarketItem> items = new ArrayList<>();

        JSONObject res = Json.parse(response);
        JSONArray data = (JSONArray) res.get("data");

        for (Object item : data) {
            items.add(MarketItem.from(
                (JSONObject) item
            ));
        }

        return items;
    }

    protected static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
