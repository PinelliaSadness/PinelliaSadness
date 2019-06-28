package com.summer.thread.my;

import java.util.concurrent.*;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/28 15:10
 */
public class MyThread {

    public static void main(String[] args) {
        // 核心线程池的大小
        int corePoolSize = 10;
        // 巨大压力下线程池最大大小
        int maximumPoolSize = 15;
        // 空闲非核心线程回收时间 如果设置allowCoreThreadTimeOut = true，则会作用于核心线程
        long keepAliveTime = 30L;
        // 空闲非核心线程回收时间的单位
        TimeUnit unit = TimeUnit.SECONDS;
        /*
         * SynchronousQueue：
         * 这个队列接收到任务的时候，会直接提交给线程处理，而不保留它，如果所有线程都在工作怎么办？
         * 那就新建一个线程来处理这个任务！所以为了保证不出现<线程数达到了maximumPoolSize
         * 而不能新建线程>的错误，使用这个类型队列的时候，maximumPoolSize一般指定成Integer.MAX_VALUE，即无限大
         *
         * LinkedBlockingQueue：
         * 这个队列接收到任务的时候，如果当前线程数小于核心线程数，则新建线程(核心线程)处理任务；
         * 如果当前线程数等于核心线程数，则进入队列等待。
         * 由于这个队列没有最大值限制，即所有超过核心线程数的任务都将被添加到队列中，这也就导致了maximumPoolSize的设定失效，
         * 因为总线程数永远不会超过corePoolSize
         * 如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE
         *
         * ArrayBlockingQueue：
         * 可以限定队列的长度，接收到任务的时候，如果没有达到corePoolSize的值，则新建线程(核心线程)执行任务，
         * 如果达到了，则入队等候，如果队列已满，则新建线程(非核心线程)执行任务，
         * 又如果总线程数到了maximumPoolSize，并且队列也满了，则发生错误
         *
         * DelayQueue：
         * 队列内元素必须实现Delayed接口，这就意味着你传进去的任务必须先实现Delayed接口。
         * 这个队列接收到任务时，首先先入队，只有达到了指定的延时时间，才会执行任务
         *
         */
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

        /*
         * 创建线程的方式 这里使用的是默认的 可以自定义 用来给线程起个名字
         */
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        /*
         * 抛出异常使用 一般用默认就可以,如果出现某些异常抛出有问题,需要在这看看
         */
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor( corePoolSize,
                                                                        maximumPoolSize,
                                                                        keepAliveTime,
                                                                        unit,
                                                                        workQueue,
                                                                        threadFactory,
                                                                        handler);
        MyEventTask myEventTask = new MyEventTask();
        threadPoolExecutor.execute(myEventTask);


        /*
         * 可缓存线程池
         * 1.线程数无限制(Integer.MAX_VALUE) 2的31次方减1
         * 2.有空闲线程则复用空闲线程,若无空闲线程则新建线程
         * 3.一定程度减少频繁创建.销毁线程,减少系统开销
         **/
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        /*
         * 定长线程池
         * 1.可控制线程最大并发数(同时执行的线程数)
         * 2.核心线程数等于最大线程数,也就是说没有非核心线程,非核心线程等待时间没有意义,公司的设置了等待时间
         * 3.超出的线程会在队列中等待
         **/
        // 核心线程数 == 最大线程数
        int nThreads = 10;
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(nThreads);


        /*
         * 定长线程池
         * 1.支持定时及周期性任务执行
         **/
        ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(corePoolSize);

        /*
         * 单线程化的线程池
         * 1.有且仅有一个工作线程执行任务
         * 2.所有任务按照指定顺序执行,即遵循队列的入队出队规则
         **/
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();


        HelloWorldThread helloWorldThread = new HelloWorldThread();

        try {
            int count = 100;
            for (int i = 0; i < count; i ++) {
                helloWorldThread.addQueue("你到底爱不爱我?" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
