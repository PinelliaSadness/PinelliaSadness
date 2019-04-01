package com.summersadness.serialization.serializationservice;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/1 15:45
 */
public interface Serialization {


    /***
     * 序列化
     */
    public <T> byte[] serialize(T obj);

    /***
     * 反序列化
     */
    public <T> T deserialize(byte[] date, Class<T> clazz);

}
