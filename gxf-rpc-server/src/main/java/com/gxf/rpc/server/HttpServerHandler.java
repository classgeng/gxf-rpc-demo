package com.gxf.rpc.server;

import com.gxf.rpc.model.RpcRequest;
import com.gxf.rpc.model.RpcResponse;
import com.gxf.rpc.registry.LocalRegistry;
import com.gxf.rpc.serializer.JdkSerializer;
import com.gxf.rpc.serializer.Serializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        logger.info("method:{}, uri:{}", req.method().name(), req.uri());
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();
        // 序列化请求参数
        RpcRequest rpcRequest = this.doRequest(req, serializer);
        // 处理并返回结果
        RpcResponse rpcResponse = this.doHandler(rpcRequest);
        //响应
        this.doResponse(ctx, rpcResponse, serializer);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 请求
     *
     * @param request
     * @param serializer
     */
    RpcRequest doRequest(FullHttpRequest request, Serializer serializer) {
        RpcRequest rpcRequest = null;
        try {
            byte[] requestContent = request.content().array();
            rpcRequest = serializer.deserialize(requestContent, RpcRequest.class);
            logger.info("doRequest:{}", rpcRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rpcRequest;
    }


    /**
     * 处理并返回结果
     *
     * @param rpcRequest
     */
    RpcResponse doHandler(RpcRequest rpcRequest) {
        // 构造响应结果对象
        RpcResponse rpcResponse = new RpcResponse();
        // 如果请求为 null，直接返回
        if (rpcRequest == null) {
            rpcResponse.setMessage("rpcRequest is null");
            return rpcResponse;
        }
        try {
            // 获取要调用的服务实现类，通过反射调用
            Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
            Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
            // 封装返回结果
            rpcResponse.setData(result);
            rpcResponse.setDataType(method.getReturnType());
            rpcResponse.setMessage("ok");
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setMessage(e.getMessage());
            rpcResponse.setException(e);
        }
        return rpcResponse;
    }

    /**
     * 响应
     *
     * @param rpcResponse
     */
    void doResponse(ChannelHandlerContext ctx, RpcResponse rpcResponse, Serializer serializer) {
        try {
            logger.info("doResponse:{}", rpcResponse);
            // 序列化
            byte[] serialized = serializer.serialize(rpcResponse);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(serialized));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}