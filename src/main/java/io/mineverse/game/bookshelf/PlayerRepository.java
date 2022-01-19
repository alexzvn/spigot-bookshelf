package io.mineverse.game.bookshelf;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Response;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import io.mineverse.game.foundation.ResponseDataAPI;
import io.mineverse.game.foundation.SimpleAPI;
import io.mineverse.game.utils.Config;
import io.mineverse.game.utils.Json;

public class PlayerRepository {
    protected SimpleAPI api = new SimpleAPI(
        Config.getString("bookshelf.endpoint"),
        Config.getString("bookshelf.token")
    );

    public class PlayerWalletData extends ResponseDataAPI {
        public String wallet_address = asString("wallet_address");
        public Boolean linked = asBoolean("linked");

        public PlayerWalletData(JSONObject object) {
            super(object);
        }
    }

    public PlayerWalletData fetchWallet(Player player) throws ClientProtocolException, IOException {

        String uri = "/api/bookshelf/player/:uuid/wallet";

        Response res = api.get(uri.replace(":uuid", player.getUniqueId().toString())).execute();

        int status = res.returnResponse().getStatusLine().getStatusCode();

        if (status >= 500) {
            throw new RuntimeException("Bookshelf server error: " + status);
        }

        if (status != 200) {
            return null;
        }

        return new PlayerWalletData(Json.parse(res.returnContent().asString()));
    }
}
