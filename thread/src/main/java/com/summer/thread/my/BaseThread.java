package com.summer.thread.my;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/6/28 16:03
 */
public abstract class BaseThread<E> extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(BaseThread.class);

    public Queue<E> queue = new LinkedBlockingQueue<>();

    public final Object lock = new Object();

    public boolean isLock = false;

    public BaseThread() {

    }

    public BaseThread(String threadName) {
        super(threadName);
    }

    public BaseThread(Queue<E> queue) {
        this.queue = queue;
    }

    public Queue<E> getQueue() {
        return queue;
    }

    public void setQueue(Queue<E> queue) {
        this.queue = queue;
    }

    public Object getLock() {
        return lock;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public abstract void doing();

    /**
     * 唤醒线程
     **/
    public void wakeUp(){
        synchronized (lock){
            if (isLock) {
                isLock = false;
                lock.notify();
                logger.info("唤醒线程wakeUp");
            }
        }
    }

    /**
     * 挂起线程
     **/
    public void idle() throws InterruptedException {
        if (!queue.isEmpty()){
            return;
        }

        synchronized (lock) {
            if (!isLock) {
                isLock = true;
                lock.wait();
                logger.info("挂起线程wakeUp");
            }
        }
    }

    @Override
    public void run() {
        try {
            doing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
