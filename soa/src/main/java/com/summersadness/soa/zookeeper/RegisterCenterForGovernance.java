package com.summersadness.soa.zookeeper;

import com.summersadness.soa.model.InvokerService;
import com.summersadness.soa.model.ProviderService;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 服务治理接口
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 15:33
 */
public interface RegisterCenterForGovernance {

    /***
     * 获取服务提供者列表与服务消费者列表
     */
    Pair<List<ProviderService>, List<InvokerService>> queryProviderAndInvokers(String servicename, String appKey);
}
