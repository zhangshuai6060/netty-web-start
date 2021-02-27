package com.zhang.netty.nettywebsocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * 整合 SpringBoot
 * 因为是单例的所以 只需要启动一次 就可以了
 *
 * @author fengxinglie
 * @version 1.0
 * @date 2021/2/26 15:50
 */
@Component
public class WSServer {

    private static class SingletionWSServer {
        static final WSServer instance = new WSServer();
    }

    public static WSServer getInstance() {
        return SingletionWSServer.instance;
    }

    private EventLoopGroup boosGroup;

    private EventLoopGroup workGroup;

    private ServerBootstrap server;

    private ChannelFuture future;

    public WSServer() {
        //创建两个线程组
        // 1.一个用于进行网络连接 接收的
        boosGroup = new NioEventLoopGroup();
        // 2. 另一个用于 我们实际处理 (网络通信的读写)
        workGroup = new NioEventLoopGroup();
        // 2. 通过辅助类 去构造 server
        server = new ServerBootstrap();

        // 3. 使用server绑定两个线程组
        server.group(boosGroup, workGroup)
                // 因为是server端 所以要设置 Nio
                .channel(NioServerSocketChannel.class)
                //这里 子处理器 workGroup来进行处理
                .childHandler(new WSServerInitialzer());
    }

    public void start() throws InterruptedException {
        // 4.设置 server要监听的端口 //并且设置 异步监听
        this.future = server.bind(8888).sync();
        System.err.println("netty websocket server 启动完毕...");
    }


}
