package com.gxf.rpc.consumer;

import com.gxf.rpc.api.model.User;
import com.gxf.rpc.api.service.UserService;
import com.gxf.rpc.proxy.ServiceProxyFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 简易服务消费者示例
 *
 * @author xfenggeng
 */
@Slf4j
public class ConsumerApplication {

    public static void main(String[] args) {
        // 静态代理
        //UserService userService = new UserServiceImpl();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("xfenggeng");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            log.info(newUser.getName());
        } else {
            log.info("user == null");
        }
    }
}
