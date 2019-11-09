package com.summersadness.netty.newtime;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/8 9:38
 */
public class TimeClient {

    public static void main(String[] args) {
        String host = System.getProperty("host", "127.0.0.1");
        int port = Integer.parseInt(System.getProperty("port", "8080"));
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            /***
             * 1:Bootstrap类似于ServerBootstrap，只是它用于非服务器通道，比如客户端通道或无连接通道。
             */
            Bootstrap bootstrap = new Bootstrap();
            /***
             * 2:如果您只指定一个EventLoopGroup，那么它将同时用作boss组和worker组。但是，boss工人并不用于客户端
             */
            bootstrap.group(workGroup);
            /***
             * 3:NioSocketChannel被用于创建客户端通道,而不是NioServerSocketChannel
             */
            bootstrap.channel(NioSocketChannel.class);
            /***
             * 4:注意,这里没有使用childOption(),这与使用ServerBootstrap不同,因为客户端SocketChannel没有父通道
             */
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new TimeClientHandler());
                }
            });

            /***
             * 5:我们应该调用connect()方法而不是bind()方法
             */
            // Start the client
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            // 等待直到连接关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }

    }
}
