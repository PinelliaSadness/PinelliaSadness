package com.summersadness.soa.helper;

import com.summersadness.soa.serialization.common.SerializationTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/4 16:23
 */
public class PropertyConfigHelper {

    private static final Logger logger = LoggerFactory.getLogger(PropertyConfigHelper.class);

    private static final String PROPERTY_CONFIG_CLASSPATH = "propertyconfig.properties";

    private static final Properties properties = new Properties();

    /***
     * ZK服务地址
     */
    private static String zkService;

    /***
     * ZK session超时时间
     */
    private static int zkSessionTimeout;
    
    /***
     * ZK connection超时时间
     */
    private static int zkConnectionTimeout;
    
    /***
     * 序列化算法类型
     */
    private static SerializationTypeEnum serializationTypeEnum;

    /***
     * 每个服务端提供者的Netty的连接数
     */
    private static int channelConnectSize;

    /***
     * 初始化
     */
    static {
        InputStream inputStream = null;
        try {
            inputStream = PropertyConfigHelper.class.getResourceAsStream(PROPERTY_CONFIG_CLASSPATH);
            if (null == inputStream) {
                throw new RuntimeException("propertyconfig.properties is not found");
            }
            properties.load(inputStream);

            zkService = properties.getProperty("zk_service");
            zkSessionTimeout = Integer.parseInt(properties.getProperty("zk_sessionTimeout","500"));
            zkConnectionTimeout = Integer.parseInt(properties.getProperty("zk_connectionTimeout","500"));
            channelConnectSize = Integer.parseInt(properties.getProperty("channel_connect_size","10"));
            String serialization = properties.getProperty("serialization",SerializationTypeEnum.DefaultJavaSerialization.getSerializationType());
            serializationTypeEnum = SerializationTypeEnum.queryByType(serialization);
            if (null == serializationTypeEnum) {
                throw new RuntimeException("serializationTypeEnum is null !!!");
            }

        } catch (Exception e) {
            logger.error("init PropertyConfigHelper fail !!!", e);
            throw new RuntimeException("PropertyConfigHelper init fail !!!");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String getZkService() {
        return zkService;
    }

    public static void setZkService(String zkService) {
        PropertyConfigHelper.zkService = zkService;
    }

    public static int getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public static void setZkSessionTimeout(int zkSessionTimeout) {
        PropertyConfigHelper.zkSessionTimeout = zkSessionTimeout;
    }

    public static int getZkConnectionTimeout() {
        return zkConnectionTimeout;
    }

    public static void setZkConnectionTimeout(int zkConnectionTimeout) {
        PropertyConfigHelper.zkConnectionTimeout = zkConnectionTimeout;
    }

    public static SerializationTypeEnum getSerializationTypeEnum() {
        return serializationTypeEnum;
    }

    public static void setSerializationTypeEnum(SerializationTypeEnum serializationTypeEnum) {
        PropertyConfigHelper.serializationTypeEnum = serializationTypeEnum;
    }

    public static int getChannelConnectSize() {
        return channelConnectSize;
    }

    public static void setChannelConnectSize(int channelConnectSize) {
        PropertyConfigHelper.channelConnectSize = channelConnectSize;
    }
}
