package com.summersadness.netty.io;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/5/7 10:33
 */
public class IOClient {
    public static void main(String[] args) {
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
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("127.0.0.1", 8080);
                    while (true) {
                        try {
                            socket.getOutputStream().write((System.currentTimeMillis() + ": 你好啊,朋友!").getBytes());
                            socket.getOutputStream().flush();
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
