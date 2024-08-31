package com.gxf.rpc.provider;

import com.gxf.rpc.api.service.UserService;
import com.gxf.rpc.registry.LocalRegistry;
import com.gxf.rpc.server.NettyHttpServer;

/**
 * 简易服务提供者示例
 *
 * @author xfenggeng
 */
public class ProviderApplication {

    public static void main(String[] args) throws ClassNotFoundException {
        // Class<?> userClass = Class.forName("com.gxf.rpc.provider.UserServiceImpl");
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        NettyHttpServer httpServer = new NettyHttpServer(8088);
        httpServer.start();
    }
}
