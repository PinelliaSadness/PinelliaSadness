package com.summersadness.soa.zookeeper;

import com.summersadness.soa.model.InvokerService;
import com.summersadness.soa.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * 消费端注册中心接口
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 15:34
 */
public interface RegisterCenterForInvoker {

    /***
     * 消费端初始化服务提供者信息本地缓存
     */
    void initProviderMap(String appkey, String groupName);

    /***
     * 消费端获取服务提供者信息
     */
    Map<String, List<ProviderService>> getServiceProviderDataForInvoker();

    /***
     * 消费端将消费者信息注册到zk对应的节点下
     */
    void registerInvoker(final InvokerService invokerService);
}
