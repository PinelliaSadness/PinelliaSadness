package com.summersadness.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/1 17:42
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {


    /***
     * 1:当建立连接并准备产生流量时,会调用channelActive()方法.
     *  我们会写一个代表该方法当前时间的32位整数.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        /***
         * 2:要发送新消息,我们需要分配一个包含消息的新缓冲区.因为我们将要写入一个32位的整数,
         *  因此我们需要一个ByteBuf容量至少为4个字节的空间.
         *  ByteBufAllocator通过ChannelHandlerContext.alloc()划分并分配一个新的缓冲区
         */
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));

        /***
         * 3:和往常一样，我们编写构造的消息。
         *
         *  但是，等等，翻转在哪里？我们不是曾经java.nio.ByteBuffer.flip()在NIO中发送消息之前打电话吗？ByteBuf不具有这样的方法，因为它有两个指针;
         *  一个用于读取操作，另一个用于写入操作。当您向某处写入内容时，作家索引会增加，ByteBuf而读者索引不会改变。读者索引和作者索引分别表示消息的开始和结束位置。
         *
         *  相反，NIO缓冲区没有提供一种干净的方法来确定消息内容的开始和结束位置，而无需调用flip方法。当您忘记翻转缓冲区时会遇到麻烦，因为将不会发送任何内容或发送不正确的数据。在Netty
         *  中不会发生这样的错误，因为我们对不同的操作类型有不同的指针。您会发现它使您适应它的生活变得轻松多了-无需翻身的生活！
         *
         *  要注意的另一点是ChannelHandlerContext.write()（和writeAndFlush()）方法返回一个ChannelFuture。A ChannelFuture表示尚未发生的I /
         *  O操作。这意味着，由于Netty中的所有操作都是异步的，因此可能尚未执行任何请求的操作。例如，以下代码甚至在发送消息之前就可能关闭连接：
         *
         *  Channel ch = ...;
         *  ch.writeAndFlush(message);
         *  ch.close();
         *  因此，您需要在ChannelFuture完成后调用close（）方法，该方法由write（）方法返回，并在完成写入操作时通知其侦听器,
         *  请注意，close（）也可能不会立即关闭连接，它将返回ChannelFuture。
         */
        final ChannelFuture f = ctx.writeAndFlush(time);

        /***
         * 4:当写请求完成时，我们如何得到通知？这就像将a添加ChannelFutureListener到return
         *  一样简单ChannelFuture。在这里，我们创建了一个新的匿名操作ChannelFutureListener，该匿名Channel操作在操作完成后会关闭。
         *
         *  另外，您可以使用预定义的侦听器简化代码：
         *  f.addListener(ChannelFutureListener.CLOSE);
         *  请求完成的监听
         */
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                assert f == channelFuture;
                ctx.close();

            }
        });
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
