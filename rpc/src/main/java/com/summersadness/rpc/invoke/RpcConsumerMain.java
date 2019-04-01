package com.summersadness.rpc.invoke;

import com.summersadness.rpc.framework.ConsumerProxy;
import com.summersadness.rpc.service.HelloService;

/**
 * @author PinelliaSadness@Gmail.com
 * @date 2019/2/22 15:11
 * @version 1.0.0
 */
public class RpcConsumerMain {
    public static void main(String[] args) throws Exception {
        HelloService service = ConsumerProxy.consume(HelloService.class, "127.0.0.1", 8083);
        for (int i = 0; i < 1000; i ++) {
            String hello = service.sayHello("liuyumao_" + i);
            System.out.println(hello);
            Thread.sleep(500);
        }
    }
}
