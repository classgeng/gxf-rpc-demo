package com.gxf.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        logger.info("port {} HttpServer start...", this.port);
        //netty IO主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //netty 数据处理线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            // 用于处理HTTP报文转换
                            ch.pipeline().addLast(new HttpObjectAggregator(65535));
                            // 用于处理请求，执行请求拦截器
                            ch.pipeline().addLast(new HttpServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定、监听端口
            ChannelFuture f = b.bind(port).sync();
            // 成功绑定到端口之后,给channel增加一个管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
            f.channel().closeFuture().sync();
        } finally {
            // 通道关闭时释放线程组资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8888;
        new HttpServer(port).start();
    }
}
