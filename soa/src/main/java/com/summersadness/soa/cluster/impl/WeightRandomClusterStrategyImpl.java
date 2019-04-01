package com.summersadness.soa.cluster.impl;


import com.summersadness.soa.cluster.ClusterStrategy;
import com.summersadness.soa.model.ProviderService;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 软负载加权随机算法实现
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/28 14:35
 */
public class WeightRandomClusterStrategyImpl implements ClusterStrategy {


    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //存放加权后的服务提供者列表
        List<ProviderService> providerList = new ArrayList<>();
        for (ProviderService provider : providerServices) {
            int weight = provider.getWeight();
            for (int i = 0; i < weight; i++) {
                providerList.add(provider.copy());
            }
        }

        int maxLen = providerList.size();
        int index = RandomUtils.nextInt(0, maxLen - 1);
        return providerList.get(index);
    }
}
