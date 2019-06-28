package com.summer.thread.my;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/6/28 17:04
 */
public class HelloWorldThread extends BaseThread<String> {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldThread.class);

    public HelloWorldThread (){
        super("HelloWorldThread");
        start();
    }

    @Override
    public void doing() {
        try {
            while (true) {
                String message = queue.poll();
                if (null == message) {
                    idle();
                    continue;
                }
                logger.info(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addQueue(String message) {
        this.queue.add(message);
        this.wakeUp();
    }

}
