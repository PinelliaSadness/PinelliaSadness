package com.summersadness.netty.time;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/1 17:42
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
