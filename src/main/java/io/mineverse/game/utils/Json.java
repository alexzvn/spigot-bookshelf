package io.mineverse.game.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Json {

    public static JSONObject parse(String json) {
        return (JSONObject) JSONValue.parse(json);
    }

    public static String[] toStringArray(JSONArray array) {
        String[] result = new String[array.size()];

        for (int i = 0; i < array.size(); i++) {
            result[i] = (String) array.get(i);
        }

        return result;
    }

    public static String stringify(Object object) {
        return JSONValue.toJSONString(object);
    }
}
