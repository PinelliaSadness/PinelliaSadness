package com.summersadness.netty.newtime;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/8 16:32
 */

/***
 * 1:ByteToMessageDecoder是一个实现ChannelInboundHandler,可以轻松处理碎片问题
 */
public class TimeDecoder extends ByteToMessageDecoder {

    /***
     * 2:每当接收到新的数据时,ByteToMessageDecoder都会使用内部维护的累积缓冲区来调用decode()方法
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            /***
             * 3:decode()可以决定在累积缓冲区中没有足够数据的地方不添加任何内容.
             *  当接收到更多数据时,ByteToMessageDecoder将再次调用decode()
             */
            return;
        }

        /***
         * 4:如果decode()将一个对象添加到out中,则意味着解码器成功地解码了一条消息.
         *  ByteToMessageDecoder将放弃累积缓冲区的读取部分.请记住,您不需要解码多个消息。
         *  ByteToMessageDecoder将继续调用decode()方法,直到它没有添加任何内容为止
         */
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
