package com.aihoo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public final class JacksonHelper {
    private final static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static <T> String objectToJson(T t) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("jackson object to json field! cause:{}", e.getMessage());
        }
        return json;
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("jackson json to Object field! cause:{}", e.getMessage());
        }
        return t;
    }

    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        List<T> lists = null;
        try {
            lists = objectMapper.readValue(json, getCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("jackson json to list field! cause:{}", e.getMessage());
        }
        return lists;
    }

    public static <T> Map<String, T> jsonToMap(String json) {
        Map<String, T> maps = null;
        try {
            maps = objectMapper.readValue(json, new TypeReference<Map<String, T>>() {
            });
        } catch (IOException e) {
            log.error("jackson json to map field! cause:{}", e.getMessage());
        }
        return maps;
    }

    public static <T> T mapToBean(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    public static <C, T> JavaType getCollectionType(Class<C> collectionClass, Class<T> elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
