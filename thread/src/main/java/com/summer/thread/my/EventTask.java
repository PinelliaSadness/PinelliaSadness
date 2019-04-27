package com.summer.thread.my;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/4/24 10:54
 */
public abstract class EventTask implements Runnable{

    /***
     * 该线程具体进行什么操作
     * @author PinelliaSadness@Gmail.com
     * @date 2019/4/24 11:17
     */
    public abstract void doing();

    @Override
    public void run(){
        try {
            doing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
