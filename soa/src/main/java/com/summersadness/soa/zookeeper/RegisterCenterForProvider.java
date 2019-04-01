package com.summersadness.soa.zookeeper;

import com.summersadness.soa.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * 服务端注册中心接口
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 15:35
 */
public interface RegisterCenterForProvider {

    /***
     * 服务端将服务提供者信息注册到zk对应的节点下
     */
    void registerProvider(final List<ProviderService> serviceProviderData);

    /***
     * 服务端获取服务提供者信息
     * 返回对象： Key：服务提供者接口  value：服务提供者服务方法列表
     */
    Map<String, List<ProviderService>> getServiceProviderDataForProvider();
}
