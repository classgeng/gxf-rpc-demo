package com.gxf.rpc.proxy;

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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理（JDK 动态代理）
 * @author classgeng
 */
@Slf4j
public class RegistryProxy implements InvocationHandler {

    private static String REGISTRY_URL = "http://127.0.0.1:8888";

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        // 发送请求，并返回结果
        RpcResponse rpcResponse = doHttpRequest(rpcRequest);
        if(null == rpcResponse) {
            return null;
        }
        return rpcResponse.getData();
    }

    /**
     * 发送 HTTP 请求
     *
     * @param rpcRequest
     * @return
     * @throws IOException
     */
    private RpcResponse doHttpRequest(RpcRequest rpcRequest) {
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();
        // 发送 HTTP 请求
        try {
            // 序列化（Java 对象 => 字节数组）
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            ByteArrayEntity byteEntity = new ByteArrayEntity(bodyBytes, ContentType.APPLICATION_OCTET_STREAM);
            CloseableHttpResponse response = HttpClientUtil.post(REGISTRY_URL, byteEntity);
            if (response.getCode() == 200) {
                byte[] result = EntityUtils.toByteArray(response.getEntity());
                // 反序列化（字节数组 => Java 对象）
                return serializer.deserialize(result, RpcResponse.class);
            } else {
                log.info("request_fail, httpResponse_Code:"+response.getCode()+", reasonPhrase:"+response.getReasonPhrase());
            }
        } catch (IOException e) {
            log.error("http request error: ",e);
        }
        return null;
    }
}
