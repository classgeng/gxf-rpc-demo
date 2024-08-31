package com.gxf.rpc.server;

import com.gxf.rpc.model.RpcRequest;
import com.gxf.rpc.serializer.JdkSerializer;
import com.gxf.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
public class DefaultHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        log.info("method:{}, uri:{}", req.method().name(), req.uri());
        parseHttpHeaders(req);

        // 指定序列化器
        final Serializer serializer = new JdkSerializer();
        doRequest(req, serializer);
        //响应
        this.doResponse(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.close();
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

    /**
     * 请求
     *
     * @param request
     * @param serializer
     */
    private RpcRequest doRequest(FullHttpRequest request, Serializer serializer) {
        RpcRequest rpcRequest = null;
        try {
            //byte[] requestContent = request.content().array();
            ByteBuf content = request.content();
            byte[] reqContent = new byte[content.readableBytes()];
            content.readBytes(reqContent);

            rpcRequest = serializer.deserialize(reqContent, RpcRequest.class);
            log.info("request params:{}", rpcRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rpcRequest;
    }

    private void doResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.wrappedBuffer("ok".getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response);
    }

}