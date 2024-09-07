package com.gxf.rpc.registry.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册信息类
 * @author classgeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 实现类
     */
    private Class<?> implClass;
}
