package com.summersadness.soa.model;

import java.io.Serializable;

/**
 *
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/28 14:47
 */
public class AresResponse implements Serializable {

    //UUID,唯一标识一次返回值
    private String uniqueKey;
    //客户端指定的服务超时时间
    private long invokeTimeout;
    //接口调用返回的结果对象
    private Object result;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
