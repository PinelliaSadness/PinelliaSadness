package com.summersadness.soa.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/12 18:28
 */
public class IPHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPHelper.class);

    private static String hostIp;

    static {
        String ip = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()){
                NetworkInterface networkInterface = (NetworkInterface) netInterfaces.nextElement();
                List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                for (InterfaceAddress address : interfaceAddresses) {
                    InetAddress inetAddress = address.getAddress();
                    if (null != inetAddress && inetAddress instanceof Inet4Address) {
                        if (org.apache.commons.lang3.StringUtils.equals(inetAddress.getHostAddress(), "127.0.0.1")){
                            continue;
                        }
                        ip = inetAddress.getHostAddress();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取本机IP失败:",e);
            throw new RuntimeException();
        }
        hostIp = ip;
    }

    /***
     * 获取本机IP
     * 通过获取系统所有的networkInterface网络接口 然后遍历每个网络下的InterfaceAddress组
     * 获取符合InetAddress instanceof Inet4Address 条件的一个IPV4地址
     */
    public static String getLocalIp() {
        return hostIp;
    }

    /***
     * 获取主机第一个有效IP
     */
    public static String getHostFiratIp() {
        return hostIp;
    }

    /***
     * 获取真实IP
     */
    public static String getRealIp() {
        String localIp = null;
        String netIp = null;

        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            /***
             * 是否找到外网IP
             */
            boolean finded = false;
            while (networkInterfaceEnumeration.hasMoreElements() && !finded) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    ip = addressEnumeration.nextElement();
                    if (!ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {
                        netIp = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {
                        localIp = ip.getHostAddress();
                    }
                }
            }
            if (!StringUtils.isEmpty(netIp)) {
                return netIp;
            } else {
                return localIp;
            }
        } catch (Exception e) {
            LOGGER.error("获取本机IP失败:",e);
            throw new RuntimeException();
        }
    }

    public static void main(String[] args){
        System.out.println(getLocalIp());
        System.out.println(getRealIp());
        System.out.println(getHostFiratIp());
    }
}
