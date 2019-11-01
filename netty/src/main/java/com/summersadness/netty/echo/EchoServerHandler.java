package com.summersadness.netty.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/1 17:23
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        /***
         * 1:一个ChannelHandlerContext对象提供的各种操作,使您可以触发各种I/O的事件和操作.
         *  在这里,我们调用write(Object)以逐字书写接收到的消息.请注意,我们没有像DISCARD示例那样释放收到的消息.
         *  这是因为Netty在将其写道网络时会为您释放它.重复释放会导致异常.
         */
        ctx.write(msg);
        /***
         * 2:ctx.write(Object)仅仅只是将消息写出,不会进行释放.它在内部进行缓冲,然后通过ctx.flush()释放.
         *  您可以使用更加方便快捷的ctx.writeAndFlush(msg)方法
         */
        ctx.flush();
    }

    @Override
    public void  exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 引发异常时关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
