package com.summersadness.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/8 10:46
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    /***
     * 1:一个ChannelHandler有两个生命周期侦听器方法:handlerAdded()和handlerRemoved()
     *  您可以执行任意(取消)初始化任务,只要它不会长时间阻塞即可
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release();
        buf = null;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        /***
         * 2:首先,应将所有接收到的数据累加到中buf
         */
        buf.writeBytes(byteBuf);
        byteBuf.release();

        /***
         * 3:然后,处理程序必须检查是否buf有足够的数据(在此示例中为4个字节),然后继续进行实际的业务逻辑。
         *  否则,channelRead()当有更多数据到达时,Netty将再次调用该方法,最终将累加所有4个字节
         */
        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = (byteBuf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
