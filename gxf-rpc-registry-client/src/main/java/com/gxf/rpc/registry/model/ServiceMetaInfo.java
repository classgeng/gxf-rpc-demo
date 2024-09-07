package com.gxf.rpc.registry.model;

import com.gxf.rpc.registry.constant.RpcConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务元信息（注册信息）
 * @author classgeng
 */
@Data
@NoArgsConstructor
public class ServiceMetaInfo implements Serializable {

    public ServiceMetaInfo(String serviceName){
        this.serviceName = serviceName;
    }

    public ServiceMetaInfo(String serviceName, String serviceHost, Integer servicePort){
        this.serviceName = serviceName;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
    }

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 服务分组（暂未实现）
     */
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     *
     * @return
     */
    public String getServiceKey() {
        // 后续可扩展服务分组
        //return String.format("%s:%s:%s", serviceName, serviceVersion, serviceGroup);
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     *
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
        if (serviceHost.startsWith("http")) {
            return String.format("%s:%s", serviceHost, servicePort);
        }
        return String.format("http://%s:%s", serviceHost, servicePort);
    }
}
