package com.pjj.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by XinHeng on 2018/11/22.
 * describe：建议尽量只主线程使用
 */
public class JsonUtils {
    public static final Gson gson = getGson();
    private static final JsonParser jsonParser = new JsonParser();

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory());//null
        gsonBuilder.disableHtmlEscaping();
        return gsonBuilder.create();
    }

    public static String toJsonString(Object o) {
        return gson.toJson(o);
    }

    public static <T> T parse(String s, Class<T> cls) {
        return gson.fromJson(s, cls);
    }

    public static <T> T parseList(String s) {
        Type type = new TypeToken<T>() {
        }.getType();
        return gson.fromJson(s, type);
    }

    public static JsonObject toJsonObject(String s) {
        /*if (TextUtils.isEmpty(s)) {
            return null;
        }*/
        JsonElement parse = jsonParser.parse(s);
        if (parse.isJsonObject()) {
            return parse.getAsJsonObject();
        }
        return null;
    }

    public static JsonArray toJsonArray(String s) {
        JsonElement parse;
        try {
            parse = jsonParser.parse(s);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
        if (parse.isJsonArray()) {
            return parse.getAsJsonArray();
        }
        return null;
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }

    public static class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            // TODO Auto-generated method stub
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            // TODO Auto-generated method stub
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
}
