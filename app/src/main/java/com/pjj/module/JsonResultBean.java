package com.pjj.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pjj.utils.JsonUtils;

/**
 * Created by XinHeng on 2018/11/24.
 * describe：
 */
public class JsonResultBean {
    private JsonObject data;
    private boolean result;
    private String msg;
    public JsonResultBean(String json){
        JsonObject jsonObject = JsonUtils.toJsonObject(json);
        String flag = getString(jsonObject, "flag");
        if ("1".equals(flag)) {
            result = true;
            this.data = getJsonObject(jsonObject, "data");
        }else{
            msg = getString(jsonObject, "msg");
        }
    }

    public boolean result() {
        return result;
    }
    public String getMsg(){
        return msg;
    }
    public JsonObject getData() {
        return data;
    }
    protected String getString(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        if (null != jsonElement) {
            return jsonElement.getAsString();
        }
        return null;
    }

    protected JsonObject getJsonObject(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        if (null != jsonElement && jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        return null;
    }

    protected JsonArray getJsonArray(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        if (null != jsonElement && jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        }
        return null;
    }
}
