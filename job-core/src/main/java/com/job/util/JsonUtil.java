package com.job.util;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author 子羽
 * @date 2018年3月6日
 */
public class JsonUtil {

    /**
     * 转为JSON字符串
     *
     * @param target 源对象
     * @return json字符串，null-如果无法解析
     */
    public static String toJson(Object target) {
        try {
            return JSON.toJSONString(target);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JSON字符串转为对象
     *
     * @param json json字符串
     * @param clazz 转换后的类型
     * @param <T> 数据类型
     * @return 转换后的对象。null-如果无法解析
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * JSON字符串转为对象
     *
     * @param obj json字符串
     * @param clazz 转换后的类型
     * @param <T> 数据类型
     * @return 转换后的对象。null-如果无法解析
     */
    public static <T> T toMapObj(Object obj, Class<T> clazz) {
        try {
            String json=JSON.toJSONString(obj);
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JSON字符串转为对象
     *
     * @param obj json字符串
     * @return 转换后的对象。null-如果无法解析
     */
    public static SortedMap<String, String> toObjMap(Object obj) {
        try {
            String json=JSON.toJSONString(obj);
            SortedMap<String, String> params= JSON.parseObject(json, SortedMap.class);
            if(params!=null){
                Iterator<String> it=params.keySet().iterator();
                while(it.hasNext()){
                    String key=it.next();
                    if(StringUtils.isEmpty(params.get(key))){
                        params.remove(key);
                    }
                }
            }
            return params;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JSON返回的基础数据格式
     *
     * @param code 错误码，传null则采用默认错误码
     * @param msg 错误信息
     * @return 用于输出的Map对象
     */
    public static Map<String, Object> toRespJson(String code, String msg) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("resultCode", code);
        jsonMap.put("resultMessage", msg);
        return jsonMap;
    }


}
