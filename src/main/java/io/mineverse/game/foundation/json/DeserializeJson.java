package io.mineverse.game.foundation.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public final class DeserializeJson {

    public static <Json> Json from(Class<Json> clazz, String json) {

        try {
            return construct(clazz, (JSONObject) JSONValue.parse(json));
        }

        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {

            e.printStackTrace();
        }

        return null;
    }

    protected static <Json> Json construct(Class<Json> clazz, JSONObject value) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonField.class)) {
                resolveField(field, value);
            }

            if (field.isAnnotationPresent(JsonArray.class)) {
                resolveArrayField(field, value);
            }

            if (field.isAnnotationPresent(JsonObject.class)) {
                resolveObjectField(field, value);
            }
        }

        return clazz.getDeclaredConstructor().newInstance();
    }

    protected static void resolveField(Field field, JSONObject json) throws IllegalAccessException {
        String jsonKey = field.getAnnotation(JsonField.class).key();

        jsonKey = jsonKey.isEmpty() ? field.getName() : jsonKey;

        field.setAccessible(true);

        field.set(field.getType(), field.getType().cast(json.get(jsonKey)));
    }


    protected static void resolveObjectField(Field field, JSONObject json) {
        String jsonKey = field.getAnnotation(JsonField.class).key();

        jsonKey = jsonKey.isEmpty() ? field.getName() : jsonKey;

        field.setAccessible(true);

        Class<?> type = field.getType();

        try {
            field.set(type, construct(type, (JSONObject) json.get(jsonKey)));
        }

        catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    protected static void resolveArrayField(Field field, JSONObject json) {
        String jsonKey = field.getAnnotation(JsonField.class).key();

        jsonKey = jsonKey.isEmpty() ? field.getName() : jsonKey;

        field.setAccessible(true);

        Class<?> type = field.getType();
        List<Object> values = new ArrayList<>();

        for (JSONObject value : (JSONObject[]) json.get(jsonKey)) {
            try {
                values.add(construct(type, value));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        try {
            field.set(type, values.toArray());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
