package com.aihoo.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtil {

    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            List<T> result = JSON.parseArray(json, clazz);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static String getString(String json, String key) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            return jsonObject.getString(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getIntValue(String json, String key) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            return jsonObject.getIntValue(key);
        } catch (Exception e) {
            return 0;
        }
    }

    public static <T> T getObject(String json, String key, Class<T> clazz) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            return jsonObject.getObject(key, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> getArray(String json, String key, Class<T> clazz) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            String string = jsonObject.getString(key);
            return JSON.parseArray(string, clazz);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static Map<String, String> JsonToMap(JSONObject j) {
        Map<String, String> map = new HashMap<>();
        if (j == null) return map;
        j.keySet().forEach(key -> map.put(key, String.valueOf(j.get(key))));
        return map;
    }

    public static List<Map<String, String>> getMaps(Object data) {
        if (data == null) return null;
        if (data instanceof List) {
            return JSON.parseObject(JSON.toJSONString(data), new TypeReference<List<Map<String, String>>>() {});
        } else {
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(data));
            Map<String, String> map = JsonToMap(object);
            List<Map<String, String>> list = new ArrayList<>();
            list.add(map);
            return list;
        }
    }
}