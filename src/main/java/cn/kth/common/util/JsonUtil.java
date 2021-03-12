package cn.kth.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

public class JsonUtil {

    public static Gson exclusionGson(boolean mark, final String... names) {

        GsonBuilder builder = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    // 过滤掉字段名是name的字段
                    @Override
                    public boolean shouldSkipField(
                            FieldAttributes f) {

                        for (String name : names) {
                            if (f.getName().equals(name))
                                return true;

                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {

                        return false;
                    }
                });

        //使用使用expose注解序列化
        if (mark) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        return builder.create();
    }

    public static Gson exposeGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public static <T> T fromString(String json, Class<T> cls) {
        if (null == json) {
            return null;
        }

        return new Gson().fromJson(json, cls);
    }

    public static <T> T fromString(GsonBuilder builder, String json,
                                   Class<T> cls) {
        if (null == json) {
            return null;
        }

        return builder.create().fromJson(json, cls);
    }

    public static <T> T fromString(String json, Type type) {
        if (null == json) {
            return null;
        }

        return new Gson().fromJson(json, type);
    }

    public static <T> T fromString(GsonBuilder builder, String json,
                                   Type type) {
        if (null == json) {
            return null;
        }

        return builder.create().fromJson(json, type);
    }

    public static <T> List<T> fromString2List(GsonBuilder builder,
                                              String json, Type type) {
        if (null == json) {
            return null;
        }

        return builder.create().fromJson(json, type);
    }

    public static <K, V> Map<K, V> fromString2Map(String json, Type type) {
        if (null == json) {
            return null;
        }

        return new Gson().fromJson(json, type);
    }

    public static <K, V> Map<K, V> fromString2Map(GsonBuilder builder,
                                                  String json, Type type) {
        if (null == json) {
            return null;
        }

        return builder.create().fromJson(json, type);
    }

    public static <T> String toJson(T obj) {
        if (null == obj) {
            return "";
        }

        return new Gson().toJson(obj);
    }

    public static <T> String toJson(GsonBuilder builder, T obj) {
        if (null == obj) {
            return "";
        }

        return builder.create().toJson(obj);
    }

    public static <T> T fromJson(GsonBuilder builder, JsonObject json,
                                 Class<T> cls) {
        if (null == json) {
            return null;
        }

        return builder.create().fromJson(json, cls);
    }

    public static <T> T fromJson(JsonObject json, Class<T> cls) {
        if (null == json) {
            return null;
        }

        return new Gson().fromJson(json, cls);
    }

    public static <T> T fromJson(Map<?, ?> json, Class<T> cls) {
        if (null == json) {
            return null;
        }

        Gson tool = new Gson();
        return tool.fromJson(tool.toJson(json), cls);
    }

    public static <T> T fromJsonAndDateFormat(Map<?, ?> json, Class<T> cls,
                                              String dateFormat) {
        if (null == json) {
            return null;
        }

        Gson tool = new GsonBuilder().setDateFormat(dateFormat)
                .create();
        return tool.fromJson(tool.toJson(json), cls);
    }

    public static <T> JsonElement toGsonElement(T obj) {

        return new Gson().toJsonTree(obj);
    }

    public static <T> JsonElement toGsonElement(GsonBuilder builder,
                                                T obj) {

        return builder.create().toJsonTree(obj);
    }

    public static JsonElement fromString(String json) {
        JsonParser parser = new JsonParser();
        return parser.parse(json);
    }

    public static <T> String sortEntityToString(T t,
                                                Comparator<Entry<String, JsonElement>> compare,
                                                IStreamalbe<String, JsonElement> streamable) {

        return sortToString((JsonObject) toGsonElement(t), compare,
                streamable);
    }

    public static <K, V> String sortToString(Map<K, V> map,
                                             Comparator<Entry<K, V>> compare, IStreamalbe<K, V> streamable) {

        Set<Entry<K, V>> set = sort(map, compare);

        StringBuilder sb = new StringBuilder();

        Iterator<Entry<K, V>> iter = set.iterator();
        while (iter.hasNext()) {
            sb.append(streamable.stream(iter.next()));
        }

        return sb.toString();
    }

    public static String sortToString(JsonObject json,
                                      Comparator<Entry<String, JsonElement>> compare,
                                      IStreamalbe<String, JsonElement> streamable) {
        Set<Entry<String, JsonElement>> set = sort(json, compare);

        StringBuilder sb = new StringBuilder();

        Iterator<Entry<String, JsonElement>> iter = set.iterator();
        while (iter.hasNext()) {
            sb.append(streamable.stream(iter.next()));
        }

        return sb.toString();
    }

    public static <T> Set<Entry<String, JsonElement>> sortEntity(T t,
                                                                 Comparator<Entry<String, JsonElement>> compare) {

        return sort((JsonObject) toGsonElement(t), compare);
    }

    public static <K, V> Set<Entry<K, V>> sort(Map<K, V> map,
                                               Comparator<Entry<K, V>> compare) {

        Set<Entry<K, V>> set = new TreeSet<Entry<K, V>>(compare);

        set.addAll(map.entrySet());

        return set;
    }

    public static Set<Entry<String, JsonElement>> sort(JsonObject json,
                                                       Comparator<Entry<String, JsonElement>> compare) {

        if (null == compare) {
            compare = DefaultJsonComparator.INSTANCE;
        }

        Set<Entry<String, JsonElement>> set = new TreeSet<Entry<String, JsonElement>>(
                compare);

        set.addAll(json.entrySet());

        return set;
    }

    public static interface IStreamalbe<K, V> {
        String stream(Entry<K, V> item);
    }

    private static enum DefaultJsonComparator
            implements Comparator<Entry<String, JsonElement>> {

        INSTANCE;

        @Override
        public int compare(Entry<String, JsonElement> o1,
                           Entry<String, JsonElement> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }

    }
}
