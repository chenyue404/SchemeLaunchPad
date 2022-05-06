package com.cy.schemelaunchpad.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.mason.libs.utils.json.IntegerDefaultAdapter;
import com.mason.libs.utils.json.LongDefaultAdapter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Gson实现的JsonUtil
 */
public class JsonUtil {

    private final static Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setExclusionStrategies(new GsonExclusionStrategy())
            .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
            .registerTypeAdapter(String.class, new StringNullAdapter())
            .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
            .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
            .registerTypeAdapter(List.class, new ListTypeAdapter())
            .registerTypeAdapter(Float.class, new FloatTypeAdapter())
            .registerTypeAdapter(long.class, new LongDefaultAdapter())
//            .disableHtmlEscaping()
            .create();

    private JsonUtil() {
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(byte[] bytes, Class<T> clazz) {
        return GSON.fromJson(new String(bytes), clazz);
    }

    public static <T> T fromJson(JsonElement json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(JsonElement json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(JsonReader json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(JsonReader json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T convert(String jsonString, Class<T> classOfT) {
        if (jsonString == null || jsonString.length() <= 0) {
            return null;
        }
        return GSON.fromJson(jsonString, classOfT);
    }
}
