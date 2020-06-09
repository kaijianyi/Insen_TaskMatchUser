package com.insenuser.utils;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class JsonUtils {

    /**
     * GSON转换为json
     * 
     * @param obj
     * @return
     */
    public static String objToGson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * GSON转为实体类型
     * 
     * @param jsonStr
     * @param obj
     * @return
     */
    public static <T> T gsonToObj(String jsonStr, Type typeOfT) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, typeOfT);
    }

    /**
     * Fastjson转换为json
     * 
     * @param obj
     * @return
     */
    public static String objToFastjson(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * Fastjson转为实体类型
     * 
     * @param jsonStr
     * @param obj
     * @return
     */
    public static <T> T fastjsonToObj(String jsonStr, Type typeOfT) {
        return JSONObject.parseObject(jsonStr, typeOfT);
    }

}
