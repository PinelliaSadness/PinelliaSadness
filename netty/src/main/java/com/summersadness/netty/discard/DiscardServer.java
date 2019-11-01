package com.summersadness.netty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/10/31 17:35
 * 丢弃所有传入数据的服务器 Discard any incoming data
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port){
        this.port = port;
    }

    public void run() throws Exception {

        /***
         * 1:NioEventLoopGroup是处理I/O操作的多线程事件循环.Netty为不同类型的传输提供了各种EventLoopGroup实现
         *  在这里,我们实现了一个服务器段应用程序,因此将使用两个NioEventLoopGroup.
         *  第一个通常被成为"boss",接受传入的连接.
         *  第二个通常呗称为"worker",在boss接受连接并将接受的连接注册到worker后,它将处理接受的连接的通信
         *  使用多少线程以及如何将它们映射到创建的通道取决于EventLoopGroup的实现,你可以通过构造函数进行配置
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            /***
             * 2:ServerBootstrap是设置服务器的帮助程序类.您可以直接使用Channel设置服务器,
             *  但是,请注意,这是一个单调乏味的过程,在大多数情况下您不需要这样做.
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    /***
                     * 3:在这里,我们指定使用NioServerSocketChannel用于实例化新类Channel以接受传入连接的类.
                     */
                    .channel(NioServerSocketChannel.class)
                    /***
                     * 4:这里指定的处理程序将始终由新接受的通道计算.ChannelInitializer是用于帮助用户配置新通道的特殊处理程序
                     *  您很可能希望通过添加一些处理程序(如DisCardServerHandler)来配置新通道的ChannelPipeline
                     *  来实现您的网络应用程序.随着应用越来越复杂,您可能会向Pipeline(管道/通道)中添加更多的处理程序(Handler),
                     *  并最终将此匿名类提取到顶级类中.
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    /***
                     * 5:您还可以设置特定于Channel实现的参数.我们正在编写TCP/IP服务器,因此我们可以设置套接字选项,
                     *  例如tcpNoDelay和keepAlive.
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)
                    /***
                     * 6:option()用于接受传入连接的NioServerSocketChannel.
                     *  childOption()用于父级ServerChannel接受的通道,在这里是NioServerSocketChannel
                     */
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定并开始接受传入的连接
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 等待服务器套接字关闭
            // 在这个示例中,不会发生需要等待关闭的情况,但是我们可以等
            // 正常关闭服务器
            channelFuture.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }
}
