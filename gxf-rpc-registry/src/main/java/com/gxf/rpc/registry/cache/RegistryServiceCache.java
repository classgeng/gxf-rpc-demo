package com.gxf.rpc.registry.cache;

import com.gxf.rpc.registry.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServiceCache {

    /**
     * 注册信息存储
     */
    private static final Map<String, List<ServiceMetaInfo>> registryMap = new ConcurrentHashMap<>();

    public static List<ServiceMetaInfo> get(String serviceKey){
        return registryMap.get(serviceKey);
    }

    public static void put(String serviceKey, List<ServiceMetaInfo> serviceMetaInfoList){
        registryMap.put(serviceKey, serviceMetaInfoList);
    }

    public static long size(){
        return registryMap.size();
    }

}
