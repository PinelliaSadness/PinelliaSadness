package com.summersadness.soa.model;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 服务注册中心的服务提供者注册信息
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 10:38
 */
public class ProviderService implements Serializable {

    /***
     * 服务接口
     */
    private Class<?> serviceInterface;

    /***
     * 服务实现
     */
    private transient Object serviceObject;

    /***
     * 方法
     */
    private transient Method serviceMethod;

    /***
     * IP地址
     */
    private String serverIp;

    /***
     * 服务端口
     */
    private int serverPort;
    
    /***
     * 服务超时时间
     */
    private long timeout;
    
    /***
     * 服务代理对象，暂时没有用到
     */
    private Object serviceProxyObject;

    /***
     * 服务提供者唯一标识
     */
    private String appKey;

    /***
     * 服务分组组名
     */
    private String groupName = "default";

    /***
     * 服务提供者权重，默认为1，范围【1-100】
     */
    private int weight = 1;

    /***
     * 服务端线程数，默认10个县城，实现服务端流控
     */
    private int workerThreads = 10;

    public ProviderService copy() {
        ProviderService providerService = new ProviderService();
        providerService.setServiceInterface(serviceInterface);
        providerService.setServiceObject(serviceObject);
        providerService.setServiceMethod(serviceMethod);
        providerService.setServerIp(serverIp);
        providerService.setServerPort(serverPort);
        providerService.setTimeout(timeout);
        providerService.setServiceProxyObject(serviceProxyObject);
        providerService.setAppKey(appKey);
        providerService.setGroupName(groupName);
        providerService.setWeight(weight);
        providerService.setWorkerThreads(workerThreads);
        return providerService;
    }
    
    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Object getServiceProxyObject() {
        return serviceProxyObject;
    }

    public void setServiceProxyObject(Object serviceProxyObject) {
        this.serviceProxyObject = serviceProxyObject;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }
}
