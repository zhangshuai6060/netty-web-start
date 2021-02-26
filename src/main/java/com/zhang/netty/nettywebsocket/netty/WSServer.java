package com.zhang.netty.nettywebsocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author fengxinglie
 * @version 1.0
 * @date 2021/2/26 15:50
 */

public class WSServer {

    public static void main(String[] args) throws InterruptedException {

        //创建两个线程组
        // 1.一个用于进行网络连接 接收的
        // 2. 另一个用于 我们实际处理 (网络通信的读写)
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try{

            // 2. 通过辅助类 去构造 server
            ServerBootstrap server = new ServerBootstrap();

            // 3. 使用server绑定两个线程组
            server.group(bossGroup,workGroup)
                    // 因为是server端 所以要设置 Nio
                    .channel(NioServerSocketChannel.class)
                    //这里 子处理器 workGroup来进行处理
                    .childHandler(new WSServerInitialzer());
            // 4.设置 server要监听的端口 //并且设置 异步监听
            ChannelFuture sync = server.bind(8888).sync();

            sync.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

}
