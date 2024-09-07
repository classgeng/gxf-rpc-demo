package com.gxf.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * netty http server
 * @author xfenggeng
 */
@Slf4j
public class NettyHttpServer {
    private int port;

    private ChannelInboundHandlerAdapter handler;

    public NettyHttpServer(int port) {
        this.port = port;
        this.handler = new NettyHttpServerHandler();
    }

    public NettyHttpServer(int port, ChannelInboundHandlerAdapter handler) {
        this.port = port;
        this.handler = null == handler ? new NettyHttpServerHandler() : handler;
    }

    /**
     * 启动服务
     */
    public void start() {
        log.info("port {} HttpServer start...", this.port);
        //netty IO主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //netty 数据处理线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            // 用于处理HTTP报文转换
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));
                            // 用于处理请求，执行请求拦截器
                            ch.pipeline().addLast(handler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定、监听端口
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("port {} HttpServer success...", this.port);
            // 成功绑定到端口之后,给channel增加一个管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
            future.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("doStart error",e);
        }finally {
            // 通道关闭时释放线程组资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
