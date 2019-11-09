package com.summersadness.netty.newtime;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/9 9:28
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int)m.value());
        ctx.write(encoded, promise);
    }
}
//  同样的效果
//public class TimeEncoder extends MessageToByteEncoder<UnixTime> {
//    @Override
//    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
//        out.writeInt((int)msg.value());
//    }
//}