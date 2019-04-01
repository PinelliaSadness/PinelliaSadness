package com.summersadness.rpc.service;

/**
 * @author PinelliaSadness@Gmail.com
 * @date 2019/2/22 15:13
 * @version 1.0.0
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String content) {
        return "Hello, " + content;
    }
}
