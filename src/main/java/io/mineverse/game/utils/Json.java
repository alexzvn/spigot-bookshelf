package io.mineverse.game.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Json {

    public static JSONObject parse(String json) {
        return (JSONObject) JSONValue.parse(json);
    }

    public static String stringify(Object object) {
        return JSONValue.toJSONString(object);
    }
}
