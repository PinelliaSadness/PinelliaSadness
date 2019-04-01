package com.summersadness.soa.cluster;

import com.summersadness.soa.model.ProviderService;

import java.util.List;

/**
 *
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/28 14:46
 */
public interface ClusterStrategy {

    /**
     * 负载策略算法
     */
    ProviderService select(List<ProviderService> providerServices);
}
