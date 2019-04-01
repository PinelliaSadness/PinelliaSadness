package com.summersadness.soa.provider;

import com.summersadness.soa.helper.PropertyConfigHelper;
import com.summersadness.soa.model.AresRequest;
import com.summersadness.soa.serialization.NettyDecoderHandler;
import com.summersadness.soa.serialization.common.SerializationTypeEnum;
import com.summersadness.soa.serialization.NettyEncoderHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/4 16:03
 */
public class NettyServer {

    private static NettyServer nettyServer = new NettyServer();

    private Channel channel;

    /***
     * 服务端boss线程组
     */
    private EventLoopGroup bossGroup;

    /***
     * 服务端worker线程组
     */
    private EventLoopGroup workerGroup;

    /***
     * 序列化类型配置信息
     */
    private SerializationTypeEnum serializationTypeEnum = PropertyConfigHelper.getSerializationTypeEnum();

    private NettyServer () {

    }

    //TODO 这个单例写得好像有问题
    public static NettyServer singleton() {
        return nettyServer;
    }

    /***
     * 启动Netty服务
     */
    public void start (final int port ) {
        synchronized (NettyServer.class) {
            if (null != bossGroup || null != workerGroup) {
                return;
            }

            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap  serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioSctpServerChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            /***
                             * 注册解码器NettyDecoderHander
                             */
                            socketChannel.pipeline().addLast(new NettyDecoderHandler(AresRequest.class, serializationTypeEnum));
                            /***
                             * 注册编码器NettyEncoderHandler
                             */
                            socketChannel.pipeline().addLast(new NettyEncoderHandler(serializationTypeEnum));
                            /***
                             * 注册服务端业务逻辑处理器NettyServerInvokeHandler
                             */
                        }
                    });

        }

    }
}
