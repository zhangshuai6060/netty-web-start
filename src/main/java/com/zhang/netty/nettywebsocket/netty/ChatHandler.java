package com.zhang.netty.nettywebsocket.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * 处理 消息到来的 handler
 *
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 *
 * @author fengxinglie
 * @version 1.0
 * @date 2021/2/26 18:23
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 这里应该 写 两个 channel
    // 用于记录和管理所有客户端的channle
    // 这里先使用 一个 看一下 能否 把两个的区分开来
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //如何拿到当前 连接的 人?

        // 获取客户端传输过来的消息
        String content = msg.text();

        System.out.println("接受到的数据: " + content);


        //下面这个方法 给客户端 回应消息
        clients.writeAndFlush(
                new TextWebSocketFrame(
                        "[服务器在]" + LocalDateTime.now()
                                + "接受到消息, 消息为：" + content));


    }

    /**
     * 当客户端 连接的时候 进入当前方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //当我在这里 建立的 连接的时候 如何判断 是前端还是 后端
        clients.add(ctx.channel());
    }

    /**
     * 当客户端 断开连接的时候 进入当前方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        String channelId = ctx.channel().id().asShortText();
        System.out.println("客户端被移除，channelId为：" + channelId);
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        clients.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        clients.remove(ctx.channel());
    }
}
