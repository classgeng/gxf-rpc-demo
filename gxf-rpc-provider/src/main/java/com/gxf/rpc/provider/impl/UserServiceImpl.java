package com.gxf.rpc.provider.impl;

import com.gxf.rpc.api.model.User;
import com.gxf.rpc.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户服务实现类
 *
 * @author xfenggeng
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public User getUser(User user) {
        logger.info("用户名：{}", user.getName());
        return user;
    }

    @Override
    public String sayHello(String name) {
        return null;
    }
}
