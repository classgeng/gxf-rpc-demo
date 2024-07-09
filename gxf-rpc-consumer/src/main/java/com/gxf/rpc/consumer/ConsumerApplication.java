package com.gxf.rpc.consumer;

import com.gxf.rpc.api.model.User;
import com.gxf.rpc.api.service.UserService;

/**
 * 简易服务消费者示例
 *
 * @author xfenggeng
 */
public class ConsumerApplication {

    public static void main(String[] args) {
        // 静态代理
        UserService userService = new UserServiceImpl();
        // 动态代理
        //UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("xfenggeng");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
