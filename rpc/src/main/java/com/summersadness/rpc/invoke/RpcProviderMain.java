package com.summersadness.rpc.invoke;

import com.summersadness.rpc.framework.ProvideReflect;
import com.summersadness.rpc.service.HelloService;
import com.summersadness.rpc.service.HelloServiceImpl;

/**
 * @author PinelliaSadness@Gmail.com
 * @date 2019/2/22 15:12
 * @version 1.0.0
 */
public class RpcProviderMain {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        ProvideReflect.provider(service, 8083);
    }

}
