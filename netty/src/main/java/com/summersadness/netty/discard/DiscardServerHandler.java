package com.summersadness.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/10/31 16:41
 * 处理服务器端通道 Handler a server-side channel
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    /***
     * 1:ChannelInboundHandlerAdapter是ChannelInboundHandler的实现 ,DiscardServerHandler继承ChannelInboundHandlerAdapter
     *  ChannelInboundHandler提供了可以覆盖的各种事件处理程序方法.
     *  扩展ChannelInboundHandler提供的实现处理程序接口足以满足要求
     * 2:我们在channelRead()这里重写时间处理程序方法.每当从客户端接收到心数据时,就使用接收到的消息调用此方法.
     *  在DiscardServerHandler中,接收到的消息的类型为ByteBuf
     * 3:为了实现DISCARD协议,处理程序必须忽略接收到的信息.ByteBuf是引用计数对象,必须通过该release()方法显式释放它.
     *  释放任何传递给处理程序的引用技术对象是处理程序的责任.
     * 4:当Netty由于I/O错误引发异常,或者处理程序的处理事件引发异常是,调用exceptionCaught()事件处理方法.
     *  在大多数情况下,应该记录捕获的异常,并在这里关闭这个异常所关联的连接通道.具体情况取决于需要处理特殊情况时所要采取的措施.
     *  比如,可以在关闭连接前发送带有错误代码的响应信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){

        ByteBuf in = (ByteBuf) msg;
        try {
            // 要做的具体的操作
            while (in.isReadable()) {
                System.out.println((char) in.readByte());
                System.out.flush();
            }

        } finally {
            // 释放
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void  exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 引发异常时关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
