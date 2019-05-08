package com.summersadness.netty.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/5/4 10:25
 */
public class IOServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 0L;
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

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // (1) 阻塞方法获取新的连接
                        Socket socket = serverSocket.accept();

                        // (2) 每一个新的连接都创建一个线程，负责读取数据
                        threadPoolExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    byte[] data = new byte[1024];
                                    InputStream inputStream = socket.getInputStream();
                                    while (true) {
                                        int len;
                                        // (3) 按字节流方式读取数据
                                        while ((len = inputStream.read(data)) != -1) {
                                            System.out.println(new String(data, 0, len));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
