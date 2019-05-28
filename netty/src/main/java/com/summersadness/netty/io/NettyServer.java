package com.summersadness.netty.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/5/7 16:42
 */
public class NettyServer {
    public static void main(String[] args) {
        
        /* 
         * 创建ServerBootStrap实例
         **/
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap
                /*
                 * 设置并绑定Reactor线程池:EventLoopGroup, EventLoop就是处理所有注册到本线程的Selector上面的Channel
                 **/
                .group(boss, worker)
                /* 
                 * 设置并绑定服务端的channel
                 **/
                .channel(NioServerSocketChannel.class)
                /* 
                 * 
                 **/
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println(s);
                            }
                        });
                    }
                })
                .bind(8080);

    }
}
