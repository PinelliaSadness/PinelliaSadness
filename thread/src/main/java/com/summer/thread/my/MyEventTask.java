package com.summer.thread.my;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/4/24 15:07
 */
public class MyEventTask extends EventTask{
    @Override
    public void doing() {
        while (true) {
            System.out.println("my running ");
        }
    }
}