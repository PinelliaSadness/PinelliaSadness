package com.summersadness.soa.model;

import java.io.Serializable;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 10:11
 */
public class AresRequest implements Serializable {

    // UUID，唯一标识一直返回值
    private String uuidKey;

    // 服务提供者信息
    private ProviderService providerService;

    // 调用方法名称
    private String invokeMethodName;

    // 传递参数
    private Object[] args;

    // 消费段应用名
    private String appname;

    // 消费请求超时时长
    private long invokeTimeout;

    public String getUuidKey() {
        return uuidKey;
    }

    public void setUuidKey(String uuidKey) {
        this.uuidKey = uuidKey;
    }

    public ProviderService getProviderService() {
        return providerService;
    }

    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    public String getInvokeMethodName() {
        return invokeMethodName;
    }

    public void setInvokeMethodName(String invokeMethodName) {
        this.invokeMethodName = invokeMethodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }
}
