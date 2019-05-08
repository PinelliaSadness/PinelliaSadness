package com.summersadness.netty.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/5/8 10:53
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap clientBootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        clientBootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                });

        Channel channel = clientBootstrap.connect("127.0.0.1", 8080).channel();

        while (true) {
            channel.writeAndFlush(System.currentTimeMillis() + ": 你好啊,朋友!");
            Thread.sleep(2000);
        }

    }
}
