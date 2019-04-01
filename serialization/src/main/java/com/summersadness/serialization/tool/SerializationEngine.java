package com.summersadness.serialization.tool;

import com.summersadness.serialization.serializationservice.Serialization;
import com.summersadness.serialization.serializationservice.impl.DefaultJavaSerializationImpl;
import com.summersadness.serialization.serializationservice.impl.FastJsonSerializationImpl;
import com.summersadness.serialization.serializationservice.impl.HessianSerializationImpl;
import com.summersadness.serialization.type.SerializationTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/3 16:08
 */
public class SerializationEngine {
    public static final Map<SerializationTypeEnum, Serialization> SERIALIZATION_MAP = new HashMap<>();

    //注册序列化工具到SERIALIZATION_MAP
    static {
        SERIALIZATION_MAP.put(SerializationTypeEnum.DefaultJavaSerialization, new DefaultJavaSerializationImpl());
        SERIALIZATION_MAP.put(SerializationTypeEnum.FastJsonSerialization, new FastJsonSerializationImpl());
        SERIALIZATION_MAP.put(SerializationTypeEnum.HessianSerialization, new HessianSerializationImpl());
    }

    public static <T> byte[] serialize(T obj, String serializationType) {
        SerializationTypeEnum serializationTypeEnum = SerializationTypeEnum.queryByType(serializationType);
        if (null == serializationTypeEnum) {
            throw new RuntimeException("serializationTypeEnum is null");
        }
        Serialization serialization = SERIALIZATION_MAP.get(serializationTypeEnum);
        if (null == serialization) {
            throw new RuntimeException("serialization is null");
        }
        try {
            return serialization.serialize(obj);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(byte[] date, Class<T> clazz, String serializationType) {
        SerializationTypeEnum serializationTypeEnum = SerializationTypeEnum.queryByType(serializationType);
        if (null == serializationTypeEnum) {
            throw new RuntimeException("serializationTypeEnum is null");
        }
        Serialization serialization = SERIALIZATION_MAP.get(serializationTypeEnum);
        if (null == serialization) {
            throw new RuntimeException("serialization is null");
        }
        try {
            return serialization.deserialize(date,clazz);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
