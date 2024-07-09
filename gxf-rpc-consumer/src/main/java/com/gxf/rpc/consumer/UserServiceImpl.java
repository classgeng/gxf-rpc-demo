package com.gxf.rpc.consumer;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gxf.rpc.api.model.User;
import com.gxf.rpc.api.service.UserService;
import com.gxf.rpc.model.RpcRequest;
import com.gxf.rpc.model.RpcResponse;
import com.gxf.rpc.serializer.JdkSerializer;
import com.gxf.rpc.serializer.Serializer;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.io.IOException;

/**
 * 用户服务静态代理
 *
 * @author xfenggeng
 */
public class UserServiceImpl implements UserService {

    @Override
    public String sayHello(String name) {
        return null;
    }

    @Override
    public User getUser(User user) {
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            // 序列化（Java 对象 => 字节数组）
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 发送请求
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8081")
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化（字节数组 => Java 对象）
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return (User) rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
