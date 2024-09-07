package com.gxf.rpc.lb;

import com.gxf.rpc.registry.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（消费端使用）
 * @author classgeng
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     *
     * @param serviceMetaInfoList 可用服务列表
     * @param requestParams       请求参数
     * @return
     */
    ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList, Map<String, Object> requestParams);
}
