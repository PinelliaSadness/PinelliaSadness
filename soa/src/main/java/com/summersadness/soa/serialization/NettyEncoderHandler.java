package com.summersadness.soa.serialization;

import com.summersadness.soa.serialization.common.SerializationTypeEnum;
import com.summersadness.soa.serialization.engine.SerializationEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 14:56
 */
public class NettyEncoderHandler extends MessageToByteEncoder {

    /***
     * 序列化类型
     */
    private SerializationTypeEnum serializationTypeEnum;

    public NettyEncoderHandler(SerializationTypeEnum serializationTypeEnum){
        this.serializationTypeEnum = serializationTypeEnum;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 将对象序列化为字节数组
        byte[] data = SerializationEngine.serialize(o, serializationTypeEnum.getSerializationType());
        // 将字节数组(消息体)的长度作为消息头写入，解决半包/粘包问题
        byteBuf.writeInt(data.length);
        // 写入序列化后得到的字节数组
        byteBuf.writeBytes(data);
    }
}
