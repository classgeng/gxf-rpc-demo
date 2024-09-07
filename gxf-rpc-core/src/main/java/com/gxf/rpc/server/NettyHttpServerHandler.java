package com.gxf.rpc.server;

import com.gxf.rpc.model.RpcRequest;
import com.gxf.rpc.model.RpcResponse;
import com.gxf.rpc.registry.LocalRegistry;
import com.gxf.rpc.serializer.JdkSerializer;
import com.gxf.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
@ChannelHandler.Sharable
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        log.info("method:{}, uri:{}", req.method().name(), req.uri());
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();
        // 序列化请求参数
        RpcRequest rpcRequest = this.doRequest(req, serializer);
        log.info("rpcRequest info: " + rpcRequest.toString());
        // 处理并返回结果
        RpcResponse rpcResponse = this.doHandler(rpcRequest);
        log.info("rpcResponse info: " + rpcResponse.toString());
        //响应
        this.doResponse(ctx, rpcResponse, serializer);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.close();
    }

    /**
     * 请求
     *
     * @param request
     * @param serializer
     */
    private RpcRequest doRequest(FullHttpRequest request, Serializer serializer) {
        RpcRequest rpcRequest = null;
        try {
            ByteBuf content = request.content();
            byte[] reqContent = new byte[content.readableBytes()];
            content.readBytes(reqContent);

            rpcRequest = serializer.deserialize(reqContent, RpcRequest.class);
            log.info("doRequest:{}", rpcRequest);
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
    private RpcResponse doHandler(RpcRequest rpcRequest) {
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
            log.error("doHandler error: " + e);
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
    private void doResponse(ChannelHandlerContext ctx, RpcResponse rpcResponse, Serializer serializer) {
        try {
            log.info("doResponse:{}", rpcResponse);
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

    /**
     * 打印头部信息
     * @param request
     */
    private void parseHttpHeaders(FullHttpRequest request){
        HttpHeaders httpHeaders = request.headers();
        for (Map.Entry<String, String> entry : httpHeaders.entries()) {
            log.info(entry.getKey()+":"+entry.getValue());
        }
    }

    /**
     * 打印请求体
     * @param request
     */
    private void parseBody(FullHttpRequest request){
        String reqContent = request.content().toString(Charset.defaultCharset());
        log.info("request params:{}", reqContent);
    }

}