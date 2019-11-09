package com.summersadness.netty.newtime;

import java.util.Date;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/11/8 18:05
 */
public class UnixTime {
    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}
