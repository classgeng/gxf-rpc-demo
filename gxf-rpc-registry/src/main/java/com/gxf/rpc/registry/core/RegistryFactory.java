package com.gxf.rpc.registry.core;

/**
 * 注册中心工厂（用于获取注册中心对象）
 * @author classgeng
 */
public class RegistryFactory {

    /**
     * 获取实例
     * @param registryType
     * @return
     */
    public static Registry getInstance(RegistryType registryType) {
        Registry registry = null;
        switch (registryType) {
            case NACOS:
                registry = new NacosRegistry();
                break;
            case CONSUL:
                registry = new ConsulRegistry();
                break;
            case DEFAULT:
                registry = new DefaultRegistry();
                break;
            default:
                throw new RuntimeException("暂不支持注册中心类型：" + registryType.name());
        }
        return registry;
    }

}
