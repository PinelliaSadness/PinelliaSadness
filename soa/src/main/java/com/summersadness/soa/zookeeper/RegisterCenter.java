package com.summersadness.soa.zookeeper;

import com.summersadness.soa.helper.IPHelper;
import com.summersadness.soa.helper.PropertyConfigHelper;
import com.summersadness.soa.model.InvokerService;
import com.summersadness.soa.model.ProviderService;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author PinelliaSadness@Gmail.com
 * @version 1.0.0
 * @date 2019/3/5 15:32
 */
public class RegisterCenter implements RegisterCenterForGovernance, RegisterCenterForInvoker, RegisterCenterForProvider{

    private static RegisterCenter registerCenter = new RegisterCenter();

    /***
     * 服务提供者列表,Key:服务提供者接口 value:服务提供者方法列表
     */
    private static final Map<String, List<ProviderService>> providerServiceMap = new ConcurrentHashMap<>();

    /***
     * 服务端ZK服务元信息,选择服务
     */
    private static final Map<String, List<ProviderService>> serviceMetaDataMapForConsume = new ConcurrentHashMap<>();

    private static String ZK_SERVICE = PropertyConfigHelper.getZkService();

    private static int ZK_SESSION_TIME_OUT = PropertyConfigHelper.getZkSessionTimeout();

    private static int ZK_CONNECTION_TIME_OUT = PropertyConfigHelper.getZkConnectionTimeout();

    private static String ROOT_PATH ="/config_register";

    private static String PROVIDER_TYPE = "provider";

    private static String INVOKER_TYPE = "consumer";

    private static volatile ZkClient zkClient;

    private RegisterCenter() {

    }

    public static RegisterCenter singleton() {
        return registerCenter;
    }
    

    /***
     * 获取服务提供者列表与服务消费者列表
     * @param servicename 服务名称
     * @param appKey 服务的唯一标识
     */
    @Override
    public Pair<List<ProviderService>, List<InvokerService>> queryProviderAndInvokers(String servicename,
                                                                                      String appKey) {
//服务消费者列表
        List<InvokerService> invokerServices = new ArrayList<>();
        //服务提供者列表
        List<ProviderService> providerServices = new ArrayList<>();

        //连接zk
        if (zkClient == null) {
            synchronized (RegisterCenter.class) {
                if (zkClient == null) {
                    zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
                }
            }
        }

        String parentPath = ROOT_PATH + "/" + appKey;
        //获取 ROOT_PATH + APP_KEY注册中心子目录列表
        List<String> groupServiceList = zkClient.getChildren(parentPath);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(groupServiceList)) {
            return Pair.of(providerServices, invokerServices);
        }

        for (String group : groupServiceList) {
            String groupPath = parentPath + "/" + group;
            //获取ROOT_PATH + APP_KEY + group 注册中心子目录列表
            List<String> serviceList = zkClient.getChildren(groupPath);
            if (org.apache.commons.collections.CollectionUtils.isEmpty(serviceList)) {
                continue;
            }
            for (String service : serviceList) {
                //获取ROOT_PATH + APP_KEY + group +service 注册中心子目录列表
                String servicePath = groupPath + "/" + service;
                List<String> serviceTypes = zkClient.getChildren(servicePath);
                if (org.apache.commons.collections.CollectionUtils.isEmpty(serviceTypes)) {
                    continue;
                }
                for (String serviceType : serviceTypes) {
                    if (StringUtils.equals(serviceType, PROVIDER_TYPE)) {
                        //获取ROOT_PATH + APP_KEY + group +service+serviceType 注册中心子目录列表
                        String providerPath = servicePath + "/" + serviceType;
                        List<String> providers = zkClient.getChildren(providerPath);
                        if (org.apache.commons.collections.CollectionUtils.isEmpty(providers)) {
                            continue;
                        }

                        //获取服务提供者信息
                        for (String provider : providers) {
                            String[] providerNodeArr = StringUtils.split(provider, "|");

                            ProviderService providerService = new ProviderService();
                            providerService.setAppKey(appKey);
                            providerService.setGroupName(group);
                            providerService.setServerIp(providerNodeArr[0]);
                            providerService.setServerPort(Integer.parseInt(providerNodeArr[1]));
                            providerService.setWeight(Integer.parseInt(providerNodeArr[2]));
                            providerService.setWorkerThreads(Integer.parseInt(providerNodeArr[3]));
                            providerServices.add(providerService);
                        }

                    } else if (StringUtils.equals(serviceType, INVOKER_TYPE)) {
                        //获取ROOT_PATH + APP_KEY + group +service+serviceType 注册中心子目录列表
                        String invokerPath = servicePath + "/" + serviceType;
                        List<String> invokers = zkClient.getChildren(invokerPath);
                        if (org.apache.commons.collections.CollectionUtils.isEmpty(invokers)) {
                            continue;
                        }

                        //获取服务消费者信息
                        for (String invoker : invokers) {
                            InvokerService invokerService = new InvokerService();
                            invokerService.setAppKey(appKey);
                            invokerService.setGroupName(group);
                            invokerService.setServerIp(invoker);
                            invokerServices.add(invokerService);
                        }
                    }
                }
            }

        }
        return Pair.of(providerServices, invokerServices);
    }

    /***
     * 消费端初始化服务提供者信息本地缓存
     * @param appkey 服务的唯一标识
     * @param groupName 服务分组组名
     */
    @Override
    public void initProviderMap(String appkey, String groupName) {
        if (MapUtils.isEmpty(serviceMetaDataMapForConsume)) {
            serviceMetaDataMapForConsume.putAll(fetchOrUpdateServiceMetaData(appkey, groupName));
        }
    }

    private Map<String, List<ProviderService>> fetchOrUpdateServiceMetaData(String remoteAppKey, String groupName) {
        final Map<String, List<ProviderService>> providerServiceMap = new ConcurrentHashMap<>();
        //连接zk
        synchronized (RegisterCenter.class) {
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
            }
        }

        //从ZK获取服务提供者列表
        String providePath = ROOT_PATH + "/" + remoteAppKey + "/" + groupName;
        List<String> providerServices = zkClient.getChildren(providePath);

        for (String serviceName : providerServices) {
            String servicePath = providePath + "/" + serviceName + "/" + PROVIDER_TYPE;
            List<String> ipPathList = zkClient.getChildren(servicePath);
            for (String ipPath : ipPathList) {
                String serverIp = StringUtils.split(ipPath, "|")[0];
                String serverPort = StringUtils.split(ipPath, "|")[1];
                int weight = Integer.parseInt(StringUtils.split(ipPath, "|")[2]);
                int workerThreads = Integer.parseInt(StringUtils.split(ipPath, "|")[3]);
                String group = StringUtils.split(ipPath, "|")[4];

                List<ProviderService> providerServiceList = providerServiceMap.get(serviceName);
                if (providerServiceList == null) {
                    providerServiceList = new ArrayList<>();
                }
                ProviderService providerService = new ProviderService();

                try {
                    providerService.setServiceInterface(ClassUtils.getClass(serviceName));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                providerService.setServerIp(serverIp);
                providerService.setServerPort(Integer.parseInt(serverPort));
                providerService.setWeight(weight);
                providerService.setWorkerThreads(workerThreads);
                providerService.setGroupName(group);
                providerServiceList.add(providerService);

                providerServiceMap.put(serviceName, providerServiceList);
            }

            //监听注册服务的变化,同时更新数据到本地缓存
            zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    if (currentChilds == null) {
                        currentChilds = new ArrayList<>();
                    }
                    List<String> survivalServiceList = new ArrayList<>();
                    for (String input : currentChilds) {
                        survivalServiceList.add(StringUtils.split(input, "|")[0]);
                    }
                    refreshServiceMetaDataMap(survivalServiceList);
                }
            });
        }
        return providerServiceMap;
    }

    private void refreshServiceMetaDataMap(List<String> serviceIpList) {
        if (serviceIpList == null) {
            serviceIpList = new ArrayList<>();
        }

        Map<String, List<ProviderService>> currentServiceMetaDataMap = new HashMap<>();
        for (Map.Entry<String, List<ProviderService>> entry : serviceMetaDataMapForConsume.entrySet()) {
            String serviceItfKey = entry.getKey();
            List<ProviderService> serviceList = entry.getValue();

            List<ProviderService> providerServiceList = currentServiceMetaDataMap.get(serviceItfKey);
            if (providerServiceList == null) {
                providerServiceList = new ArrayList<>();
            }

            for (ProviderService serviceMetaData : serviceList) {
                if (serviceIpList.contains(serviceMetaData.getServerIp())) {
                    providerServiceList.add(serviceMetaData);
                }
            }
            currentServiceMetaDataMap.put(serviceItfKey, providerServiceList);
        }

        serviceMetaDataMapForConsume.clear();
        serviceMetaDataMapForConsume.putAll(currentServiceMetaDataMap);
    }

    /***
     * 消费端获取服务提供者信息
     */
    @Override
    public Map<String, List<ProviderService>> getServiceProviderDataForInvoker() {
        return serviceMetaDataMapForConsume;
    }

    /***
     * 消费端将消费者信息注册到zk对应的节点下
     * @param invokerService 消费者信息
     */
    @Override
    public void registerInvoker(InvokerService invokerService) {
        if (invokerService == null) {
            return;
        }

        //连接zk,注册服务
        synchronized (RegisterCenter.class) {

            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
            }
            //创建 ZK命名空间/当前部署应用APP命名空间/
            boolean exist = zkClient.exists(ROOT_PATH);
            if (!exist) {
                zkClient.createPersistent(ROOT_PATH, true);
            }


            //创建服务消费者节点
            String remoteAppKey = invokerService.getAppKey();
            String groupName = invokerService.getGroupName();
            String serviceNode = invokerService.getServiceInterface().getName();
            String servicePath = ROOT_PATH + "/" + remoteAppKey + "/" + groupName + "/" + serviceNode + "/" + INVOKER_TYPE;
            exist = zkClient.exists(servicePath);
            if (!exist) {
                zkClient.createPersistent(servicePath, true);
            }

            //创建当前服务器节点
            String localIp = IPHelper.getLocalIp();
            String currentServiceIpNode = servicePath + "/" + localIp;
            exist = zkClient.exists(currentServiceIpNode);
            if (!exist) {
                //注意,这里创建的是临时节点
                zkClient.createEphemeral(currentServiceIpNode);
            }
        }
    }

    /***
     * 服务端将服务提供者信息注册到zk对应的节点下
     * @param serviceProviderData 服务端信息
     */
    @Override
    public void registerProvider(final List<ProviderService> serviceProviderData) {
        if (CollectionUtils.isEmpty(serviceProviderData)) {
            return;
        }
        
        /***
         * 连接ZK,注册服务
         */
        synchronized (RegisterCenter.class) {
            for (ProviderService providerService : serviceProviderData){
                String serviceinterfaceName = providerService.getServiceInterface().getName();
                
                List<ProviderService> providerServiceList = providerServiceMap.get(serviceinterfaceName);
                if (null == providerServiceList) {
                    providerServiceList = new ArrayList<>();
                }
                providerServiceList.add(providerService);
                providerServiceMap.put(serviceinterfaceName, providerServiceList);
            }
            
            if (null == zkClient) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
            }
            
            /***
             * 创建 ZK命名空间/当前部署应用APP命名空间/
             */
            String app_key = serviceProviderData.get(0).getAppKey();
            String zk_path = ROOT_PATH + "/" + app_key;
            
            boolean exist = zkClient.exists(zk_path);
            if (!exist) {
                zkClient.createPersistent(zk_path, true);
            }
            
            for (Map.Entry<String, List<ProviderService>> entry : providerServiceMap.entrySet()) {
                /***
                 * 服务分组
                 */
                String groupName = entry.getValue().get(0).getGroupName();
                /***
                 * 创建服务提供者
                 */
                String serviceNode = entry.getKey();
                String servicePath = zk_path + "/" + groupName + "/" + serviceNode + "/" + PROVIDER_TYPE;
                exist = zkClient.exists(servicePath);
                if (!exist) {
                    zkClient.createPersistent(servicePath, true);
                }
                
                /***
                 * 创建当前服务器节点
                 */
                int serverPort = entry.getValue().get(0).getServerPort();
                int weight = entry.getValue().get(0).getWeight();
                int workerThreads = entry.getValue().get(0).getWorkerThreads();
                String localIp = IPHelper.getLocalIp();
                String currentServiceIpNode = servicePath + "/" + localIp + "|" + serverPort + "|" + weight + "|" + workerThreads + "|" + groupName;

                exist = zkClient.exists(currentServiceIpNode);
                if (!exist) {
                    /***
                     * 创建临时节点
                     */
                    zkClient.createEphemeral(currentServiceIpNode);
                }

                /***
                 * 监听注册服务的变化,同时更新数据到本地缓存
                 */
                zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                    @Override
                    public void handleChildChange(String s, List<String> list) throws Exception {
                        if (null == list) {
                            list = new ArrayList<>();
                        }

                        /***
                         * 存活的服务IP列表
                         */
                        List<String> survivalServiceList = new ArrayList<>();
                        for (String input : list) {
                            survivalServiceList.add(StringUtils.split(input, "|")[0]);
                        }
                        refreshActivityService(survivalServiceList);
                    }
                });
            }
            
        }
    }

    /***
     * 服务端获取服务提供者信息
     * 返回对象： Key：服务提供者接口  value：服务提供者服务方法列表
     */
    @Override
    public Map<String, List<ProviderService>> getServiceProviderDataForProvider() {
        return providerServiceMap;
    }

    /***
     * 利用ZK自动刷新当前存活的服务提供者列表数据
     */
    private void refreshActivityService(List<String> serviceIpList){
        if (null == serviceIpList) {
            serviceIpList = new ArrayList<>();
        }

        Map<String, List<ProviderService>> currentServiceMetaDatamap = new HashMap<>();
        for (Map.Entry<String, List<ProviderService>> entry : providerServiceMap.entrySet()) {
            String key = entry.getKey();
            List<ProviderService> providerServiceList = entry.getValue();

            List<ProviderService> serviceMetaDataModeList = currentServiceMetaDatamap.get(key);
            if (null == serviceMetaDataModeList) {
                serviceMetaDataModeList = new ArrayList<>();
            }

            for (ProviderService serviceMetaData : providerServiceList) {
                if (serviceIpList.contains(serviceMetaData.getServerIp())) {
                    serviceMetaDataModeList.add(serviceMetaData);
                }
            }
        }
        providerServiceMap.clear();
        providerServiceMap.putAll(currentServiceMetaDatamap);
    }
}
