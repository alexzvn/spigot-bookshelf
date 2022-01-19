package io.mineverse.game.foundation;

import org.json.simple.JSONObject;

public class ResponseDataAPI {
    protected JSONObject object;

    public ResponseDataAPI(JSONObject object) {
        this.object = object;
    }

    protected String asString(String key) {
        return object.get(key) != null ? object.get(key).toString() : null;
    }

    protected int asInteger(String key) {
        return object.get(key) != null ? (int) object.get(key) : null;
    }

    protected boolean asBoolean(String key) {
        return object.get(key) != null ? (boolean) object.get(key) : null;
    }

    protected float asFloat(String key) {
        return object.get(key) != null ? (float) object.get(key) : null;
    }

    protected double asDouble(String key) {
        return object.get(key) != null ? (double) object.get(key) : null;
    }

    protected ResponseDataAPI asObject(String key) {
        return object.get(key) != null ? new ResponseDataAPI((JSONObject) object.get(key)) : null;
    }

}
