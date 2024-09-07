package com.gxf.rpc.registry.core;

import com.gxf.rpc.registry.cache.RegistryServiceCache;
import com.gxf.rpc.registry.model.ServiceMetaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地服务注册中心
 */
public class DefaultRegistry implements Registry {

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfos = RegistryServiceCache.get(serviceKey);
        if(null == serviceMetaInfos){
            List<ServiceMetaInfo> serviceMetaInfoList = new ArrayList<>();
            serviceMetaInfoList.add(serviceMetaInfo);
            RegistryServiceCache.put(serviceKey, serviceMetaInfoList);
        } else {
            boolean isRegister = serviceMetaInfos.stream().anyMatch(item ->
                    item.getServiceNodeKey().equals(serviceKey));
            if(!isRegister){ // 非重复注册
                serviceMetaInfos.add(serviceMetaInfo);
                RegistryServiceCache.put(serviceKey, serviceMetaInfos);
            }
        }
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        return RegistryServiceCache.get(serviceKey);
    }

    @Override
    public void heartBeat() {

    }

    @Override
    public void watch(String serviceNodeKey) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public long size() {
        return RegistryServiceCache.size();
    }
}
