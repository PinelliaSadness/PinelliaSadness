package com.summersadness.soa.serialization.serialization.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.summersadness.soa.serialization.serialization.Serialization;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/1 17:55
 */
public class FastJsonSerializationImpl implements Serialization {
    /***
     * 序列化
     * @param obj
     */
    @Override
    public <T> byte[] serialize(T obj) {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat).getBytes();
    }

    /***
     * 反序列化
     * @param date
     * @param clazz
     */
    @Override
    public <T> T deserialize(byte[] date, Class<T> clazz) {
        return JSON.parseObject(new String(date), clazz);
    }
}
