package com.demo.chat.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description of the file purpose.
 *
 * @author zhen.ni 2024-02-10 22:37
 */
public class JsonUtils {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
        MAPPER.registerModule(javaTimeModule);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setDateFormat(new SimpleDateFormat(pattern));
    }

    private JsonUtils() {
    }

    public static String toString(Object obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to transform String", e);
        }
    }

    public static <E> List<E> toList(String json, Class<E> clazz) {
        if (StrUtil.isEmpty(json)) {
            return Collections.emptyList();
        }
        try {
            CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to transform List", e);
        }
    }

    public static Map<String, String> toMap(String json) {
        return toMap(json, String.class, String.class);
    }

    public static <V> Map<String, V> toMap(String json, Class<V> valueClass) {
        return toMap(json, String.class, valueClass);
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        JavaType type = MAPPER.getTypeFactory().constructParametricType(Map.class, keyClass, valueClass);
        JavaType typeRef = MAPPER.getTypeFactory().constructType(type);
        try {
            return MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to transform Map", e);
        }
    }

    public static <T> T toObject(String json, TypeReference<T> reference) {
        if (StrUtil.isBlank(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, reference);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to transform Object", e);
        }
    }

    public static boolean isValid(String json) {
        try {
            MAPPER.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}