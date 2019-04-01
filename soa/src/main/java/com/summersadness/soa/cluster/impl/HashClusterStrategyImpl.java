package com.summersadness.soa.cluster.impl;

import com.summersadness.soa.cluster.ClusterStrategy;
import com.summersadness.soa.helper.IPHelper;
import com.summersadness.soa.model.ProviderService;

import java.util.List;

/**
 * 软负载哈希算法实现
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/28 14:46
 */
public class HashClusterStrategyImpl implements ClusterStrategy {

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //获取调用方ip
        String localIP = IPHelper.getLocalIp();
        //获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        //获取服务列表大小
        int size = providerServices.size();

        return providerServices.get(hashCode % size);
    }
}
