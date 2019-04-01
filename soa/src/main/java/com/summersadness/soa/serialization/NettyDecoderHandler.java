package com.summersadness.soa.serialization;

import com.summersadness.soa.serialization.common.SerializationTypeEnum;
import com.summersadness.soa.serialization.engine.SerializationEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 9:12
 */
public class NettyDecoderHandler extends ByteToMessageDecoder {

    /***
     * 解码对象class
     */
    private Class<?> genericClass;

    /***
     * 解码对象编码所使用序列化类型
     */
    private SerializationTypeEnum serializationTypeEnum;

    public NettyDecoderHandler(Class<?> genericClass, SerializationTypeEnum serializationTypeEnum){
        this.genericClass = genericClass;
        this.serializationTypeEnum = serializationTypeEnum;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 获取消息头所标识的消息体字节数组长度
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0) {
            channelHandlerContext.close();
        }

        // 若当前可以获取到的字节数小于世界长度，则直接返回，直到当前可以获取到的字节数等于实际长度
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        // 读取完整的消息体字节数组
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        // 将字节数组返学劣化为java对象
        Object obj = SerializationEngine.deserialize(data, genericClass, serializationTypeEnum.getSerializationType());
        list.add(obj);
    }
}
