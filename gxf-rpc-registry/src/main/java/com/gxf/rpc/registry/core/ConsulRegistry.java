package com.gxf.rpc.registry.core;

import com.gxf.rpc.registry.model.ServiceMetaInfo;

import java.util.List;

/**
 * Consul 注册中心
 * @author classgeng
 */
public class ConsulRegistry implements Registry {

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        return null;
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
        return 0;
    }
}
