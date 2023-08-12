package site.bleem.pipeworks.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.SerializationUtils;
import scala.collection.JavaConverters;
import scala.collection.Map$;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: bangsun
 * @Date: 2021/8/17 18:18
 * @Description:
 */
public class ConvertUtil {

    public static <T> List<T> merge(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }



    public static void json2prop(JSONObject json, Properties properties) {
        String tmpKey = "";
        String tmpKeyPre = "";
        json2prop(json, tmpKey, tmpKeyPre, properties);
    }

    public static void json2prop(JSONObject jsonObject, String tmpKey, String tmpKeyPre, Properties properties) {

        Iterator<String> it = jsonObject.keySet().iterator();
        while (it.hasNext()) {
            // 获得key
            String key = it.next();
            String value = jsonObject.getString(key);
            try {
                JSONObject jsonStr = JSONObject.parseObject(value);
                tmpKeyPre = tmpKey;
                tmpKey += key + ".";
                json2prop(jsonStr, tmpKey, tmpKeyPre, properties);
                tmpKey = tmpKeyPre;
            } catch (Exception e) {
                properties.put(tmpKey + key, value);
                System.out.println(tmpKey + key + "=" + value);
                continue;
            }
        }
    }


    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public static byte[] obj2Bytes(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        bytes = bos.toByteArray();
        oos.close();
        bos.close();
        return bytes;
    }

    /**
     * 对象转键值对
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static HashMap<String, Object> obj2HashMap(Object obj) throws IOException {
        HashMap<String, Object> hashMap = SerializationUtils.deserialize(obj2Bytes(obj));
        return hashMap;
    }

    public static scala.collection.immutable.Map<String, String> map2ScalaMap(Map<String, String> map) {
        scala.collection.mutable.Map<String, String> scalaMap = JavaConverters.mapAsScalaMapConverter(map).asScala();
        Object objMap = Map$.MODULE$.<String, String>newBuilder().$plus$plus$eq(scalaMap.toSeq());
        Object BuildResObjMap = ((scala.collection.mutable.Builder) objMap).result();
        scala.collection.immutable.Map<String, String> targetScalaMap = (scala.collection.immutable.Map) BuildResObjMap;
        return targetScalaMap;
    }

    public static Map<String, String> objectToMap(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), String.valueOf(field.get(obj)));
        }
        return map;
    }

    public static JSONObject prop2Json(Properties properties) {
        JSONObject json = new JSONObject();
        Set<String> names = properties.stringPropertyNames();
        for (String name : names) {
            if (!name.contains(".")) {
                json.put(name, properties.getProperty(name));
                continue;
            }
            String[] objects = name.split("\\.");
            String lastName = objects[objects.length - 1];
            String key = name.replace("." + lastName, "");
            if (!json.containsKey(key)) {
                json.put(key, new JSONObject());
            }
            JSONObject jsonKey = json.getJSONObject(key);
            if (lastName.contains("[") && lastName.endsWith("]")) {
                String[] arrays = lastName.split("\\[");
                String arrKey = arrays[0];
                if (!jsonKey.containsKey(arrKey)) {
                    jsonKey.put(arrKey, new JSONArray());
                }
                JSONArray array = jsonKey.getJSONArray(arrKey);
                array.add(properties.getProperty(name));
                jsonKey.put(arrKey, array);
            } else {
                jsonKey.put(lastName, properties.getProperty(name));
            }
            json.put(key, jsonKey);
        }
        recJson(json);
        return json;
    }

    private static void recJson(JSONObject object) {

        List<String> keySet = object.keySet().stream().collect(Collectors.toList());
        for (String k : keySet) {
            if (!k.contains(".")) {
                continue;
            }

            String[] split = k.split("\\.");
            String lastName = split[split.length - 1];
            String key = k.replace("." + lastName, "");
            if (lastName.contains("[") && lastName.endsWith("]")) {
                String[] arrays = lastName.split("\\[");
                String arrKey = key + "." + arrays[0];
                if (!object.containsKey(arrKey)) {
                    object.put(arrKey, new JSONArray());
                }
                JSONArray array = object.getJSONArray(arrKey);
                array.add(object.get(k));
                object.put(arrKey, array);
            } else {
                if (!object.containsKey(key)) {
                    object.put(key, new JSONObject());
                }
                JSONObject jsonKey = object.getJSONObject(key);
                jsonKey.put(lastName, object.get(k));
                object.put(key, jsonKey);
            }
            object.remove(k);
        }
        long count = object.keySet().stream().filter(key -> key.contains(".")).count();
        if (count > 0) {
            recJson(object);
        }
        long arrayCount = object.keySet().stream().filter(key -> key.contains("[") && key.endsWith("]")).count();
        if (arrayCount > 0) {
            object.keySet().stream().filter(key -> key.contains("[") && key.endsWith("]")).collect(Collectors.toList()).forEach(key -> {
                String[] arrays = key.split("\\[");
                String arrKey = arrays[0];
                if (!object.containsKey(arrKey)) {
                    object.put(arrKey, new JSONArray());
                }
                JSONArray array = object.getJSONArray(arrKey);
                array.add(object.get(key));
                object.put(arrKey, array);
                object.remove(key);
            });
        }
        return;
    }


}
