package com.gxf.rpc.provider;

import com.gxf.rpc.api.service.UserService;
import com.gxf.rpc.registry.LocalRegistry;
import com.gxf.rpc.server.HttpServer;

/**
 * 简易服务提供者示例
 *
 * @author xfenggeng
 */
public class ProviderApplication {

    public static void main(String[] args) throws Exception {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new HttpServer(8081);
        httpServer.start();
    }
}
