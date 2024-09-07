package com.gxf.rpc.registry.core;

import com.gxf.rpc.registry.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心
 * @author classgeng
 */
public interface Registry {

    /**
     * 注册服务（服务端）
     *
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo);

    /**
     * 注销服务（服务端）
     *
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现（获取某服务的所有节点，消费端）
     *
     * @param serviceKey 服务键名
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 心跳检测（服务端）
     */
    void heartBeat();

    /**
     * 监听（消费端）
     *
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 注册服务数
     * @return
     */
    long size();
}
