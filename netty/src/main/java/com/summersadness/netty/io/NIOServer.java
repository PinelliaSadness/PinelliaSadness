package com.summersadness.netty.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/5/7 11:13
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        /***
         * 1:NIO模型种通常会有两个线程,每个线程绑定一个轮询器selector,
         * serverSelector负责沦胥是否有新的连接,clientSelector负责沦胥连接是否有数据可读
         *
         * 2:服务器监测到新的连接之后,不再创建一个新的线程,而是直接将心连接绑定到clientSelector上
         * 这样就不用IO模型中创建一个线程进行while循环等待.
         *
         * 3.clientSelector 被一个while死循环包裹着,如果在某个时刻有多条连接有数据可读,那么通过
         * clientSelector.select(1)方法可以沦胥出来,进而批量处理.
         *
         * 4.数据的读写以内存块为单位.
         *
         */


        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        int corePoolSize = 2;
        int maximumPoolSize = 2;
        long keepAliveTime = 10L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor( corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 对应IO编程中服务端启动
                    ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                    listenerChannel.socket().bind(new InetSocketAddress(8080));
                    listenerChannel.configureBlocking(false);
                    listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                    while (true) {
                        // 监测是否有新的连接，这里的1指的是阻塞的时间为1ms
                        if (serverSelector.select(1) > 0) {
                            Set<SelectionKey> set = serverSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();

                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();

                                if (key.isAcceptable()) {
                                    try {
                                        // (1) 每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                        SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                        clientChannel.configureBlocking(false);
                                        clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                    } finally {
                                        keyIterator.remove();
                                    }
                                }

                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        // (2) 批量轮询是否有哪些连接有数据可读，这里的1指的是阻塞的时间为1ms
                        if (clientSelector.select(1) > 0) {
                            Set<SelectionKey> set = clientSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = set.iterator();

                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();

                                if (key.isReadable()) {
                                    try {
                                        SocketChannel clientChannel = (SocketChannel) key.channel();
                                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                        // (3) 读取数据以块为单位批量读取
                                        clientChannel.read(byteBuffer);
                                        byteBuffer.flip();
                                        System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer)
                                                .toString());
                                    } finally {
                                        keyIterator.remove();
                                        key.interestOps(SelectionKey.OP_READ);
                                    }
                                }

                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
