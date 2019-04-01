package com.summersadness.serialization.type;

import org.apache.commons.lang3.StringUtils;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/3 16:14
 */
public enum SerializationTypeEnum {
    /***
     * 默认的序列化方式
     */
    DefaultJavaSerialization("DefaultJavaSerialization"),
    /***
     * Json的序列化方式
     */
    FastJsonSerialization("FastJsonSerialization"),
    /***
     * Hessian的序列化方式
     */
    HessianSerialization("HessianSerialization");

    private String serializationType;

    SerializationTypeEnum(String serializationType) {
        this.serializationType = serializationType;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public static SerializationTypeEnum queryByType(String serializationType) {
        if (StringUtils.isBlank(serializationType)) {
            return null;
        }
        for (SerializationTypeEnum serialization: SerializationTypeEnum.values()) {
            if (StringUtils.equals(serializationType, serialization.getSerializationType())) {
                return serialization;
            }
        }
        return null;
    }
}
