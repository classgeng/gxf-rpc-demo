package com.gxf.rpc.registry;

import com.gxf.rpc.registry.core.DefaultRegistry;
import com.gxf.rpc.registry.core.Registry;
import com.gxf.rpc.registry.timer.RegistreTimer;
import com.gxf.rpc.server.NettyHttpServer;

/**
 * 简易服务提供者示例
 *
 * @author xfenggeng
 */
public class RegistryApplication {

    public static void main(String[] args) throws Exception {
        //定时任务
        RegistreTimer.task();

        // 服务名称
        String serviceName = Registry.class.getName();

        //本地注册
        LocalRegistry.register(serviceName, DefaultRegistry.class);

        // 启动 web 服务
        NettyHttpServer httpServer = new NettyHttpServer(8888);
        httpServer.start();
    }
}
