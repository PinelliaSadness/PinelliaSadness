package com.summersadness.studythread;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/12/17 11:18
 */

// 以下代码来源于【参考1】
public class VolatileExample {
    int x = 0;
    volatile boolean v = false;
    public void writer() {
        x = 42;
        v = true;
    }
    public void reader() {
        if (v) {
            // 这里x会是多少呢？
            System.out.println("v=" + v);
        }
    }
}