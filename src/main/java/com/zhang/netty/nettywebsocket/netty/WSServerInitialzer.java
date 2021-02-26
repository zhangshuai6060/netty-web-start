package com.zhang.netty.nettywebsocket.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * netty管道 初始化的接口
 * <p>
 * 它的使命仅仅是为了初始化Server端新接入的SocketChannel对象，
 * 往往我们也只是初始化新接入SocketChannel的pipeline，
 * 在做完初始化工作之后，该Handler就会将自己从pipeline中移除，
 * 也就是说SocketChannel的初始化工作只会做一次。
 *
 * @author fengxinglie
 * @version 1.0
 * @date 2021/2/26 16:02
 */
public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //获取pipline
        ChannelPipeline pipeline = ch.pipeline();

        // websocket 基于http协议,所以要有 http编解码
        pipeline.addLast(new HttpServerCodec());
        //对大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 对httpMessage进行聚合 , 聚合成 FullHttpRequest或 FullHttpResponse
        // 几乎在netty中的变成,都回使用此 hanlder
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        // =====================  以上用于http协议  =============================

        // =====================  以下是支持httpWebSocket =============================

        /**
         * websocket 服务器处理的协议, 用于指定给客户端连接 访问的路由 :/ws
         * 本 handler 会帮你处理一些 繁重的复杂的事情
         * 会帮你处理握手动作 : handshaking(close,ping,pong) ping + pong = 心跳
         * 对于websocket来讲,都是以frames进行传输的,不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //设置自定义的 handler
        // 用来处理逻辑的
        pipeline.addLast(new ChatHandler());
    }
}
