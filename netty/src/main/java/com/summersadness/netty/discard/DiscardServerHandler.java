package com.summersadness.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/10/31 16:41
 * 处理服务器端通道 Handler a server-side channel
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        // 静默丢弃所有接受到的数据
        ((ByteBuf) msg).release();
    }

    @Override
    public void  exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 引发异常时关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
