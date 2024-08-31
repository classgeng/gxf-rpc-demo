package com.gxf.rpc.consumer;

import com.gxf.rpc.api.model.User;
import com.gxf.rpc.api.service.UserService;
import com.gxf.rpc.http.HttpClientUtil;
import com.gxf.rpc.model.RpcRequest;
import com.gxf.rpc.model.RpcResponse;
import com.gxf.rpc.serializer.JdkSerializer;
import com.gxf.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

/**
 * 用户服务静态代理
 *
 * @author xfenggeng
 */
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public String sayHello(String name) {
        return null;
    }

    @Override
    public User getUser(User user) {
        //请求地址
        String url = "http://localhost:8088";
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

            ByteArrayEntity byteEntity = new ByteArrayEntity(bodyBytes, ContentType.APPLICATION_OCTET_STREAM);
            CloseableHttpResponse response = HttpClientUtil.post(url, byteEntity);
            if (response.getCode() == 200) {
                byte[] result = EntityUtils.toByteArray(response.getEntity());
                // 反序列化（字节数组 => Java 对象）
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return (User) rpcResponse.getData();
            } else {
                log.info("request_fail, httpResponse_Code:"+response.getCode()+", reasonPhrase:"+response.getReasonPhrase());
            }
        } catch (IOException e) {
            log.error("http request error: ",e);
        }

        return null;
    }
}
