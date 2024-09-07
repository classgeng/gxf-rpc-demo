package com.gxf.rpc.provider;

import com.gxf.rpc.api.service.UserService;
import com.gxf.rpc.provider.impl.UserServiceImpl;
import com.gxf.rpc.proxy.ServiceProxyFactory;
import com.gxf.rpc.registry.LocalRegistry;
import com.gxf.rpc.registry.core.Registry;
import com.gxf.rpc.registry.model.ServiceMetaInfo;
import com.gxf.rpc.server.NettyHttpServer;

/**
 * 简易服务提供者示例
 *
 * @author xfenggeng
 */
public class ProviderApplication {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8088;

        // 服务名称
        String serviceName = UserService.class.getName();

        //本地注册
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 远程注册（注册中心）
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo(serviceName, host, port);
        Registry registry = ServiceProxyFactory.getRegistryProxy(Registry.class);
        registry.register(serviceMetaInfo);

        // 启动 web 服务
        NettyHttpServer httpServer = new NettyHttpServer(port);
        httpServer.start();
    }

}
