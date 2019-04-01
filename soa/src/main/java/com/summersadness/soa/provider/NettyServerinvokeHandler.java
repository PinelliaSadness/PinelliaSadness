package com.summersadness.soa.provider;

import com.summersadness.soa.model.AresRequest;
import com.summersadness.soa.model.ProviderService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 15:05
 */
@ChannelHandler.Sharable
public class NettyServerinvokeHandler extends SimpleChannelInboundHandler<AresRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerinvokeHandler.class);

    /***
     * 服务端限流
     */
    private static final Map<String, Semaphore> serviceKeySemaphoreMap = new HashMap<>();

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception{
        throwable.printStackTrace();
        // 发生异常，关闭链路
        channelHandlerContext.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AresRequest aresRequest) throws Exception {
        if (channelHandlerContext.channel().isWritable()) {
            // 从服务调用对象那里获取服务提供者的信息
            ProviderService providerService = aresRequest.getProviderService();
            long invokeTimeout = aresRequest.getInvokeTimeout();
            final String invokeMethodName = aresRequest.getInvokeMethodName();

            // 根据方法名称定位到具体某个服务提供者
            String serviceKey = providerService.getServiceInterface().getName();

            // 获取限流工具类 获取限流线程数
            int workerThread = providerService.getWorkerThreads();
            Semaphore semaphore = serviceKeySemaphoreMap.get(serviceKey);
            if (null == semaphore) {
                synchronized (serviceKeySemaphoreMap) {
                    semaphore = serviceKeySemaphoreMap.get(serviceKey);
                    if (null == semaphore) {
                        semaphore = new Semaphore(workerThread);
                        serviceKeySemaphoreMap.put(serviceKey, semaphore);
                    }
                }
            }

            // 获取注册中心服务

        }
    }
}
