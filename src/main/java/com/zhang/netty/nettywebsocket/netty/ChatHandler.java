package com.zhang.netty.nettywebsocket.netty;

import com.zhang.netty.nettywebsocket.config.FrontUserChannelRel;
import com.zhang.netty.nettywebsocket.config.ManagerUserChannelRel;
import com.zhang.netty.nettywebsocket.enums.MessageEnum;
import com.zhang.netty.nettywebsocket.util.JsonUtils;
import com.zhang.netty.nettywebsocket.vo.DataContent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * 处理 消息到来的 handler
 * <p>
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

        // 获取客户端传输过来的消息
        String content = msg.text();

        Channel currentChannel = ctx.channel();

        // 1. 获取客户端发来的消息
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        Integer action = dataContent.getAction();
        // 	2.1  action = 1 当websocket 第一次open的时候，初始化channel，把用的channel和userid关联起来
        if (action == MessageEnum.FRONT.getType()) {
            String userId = dataContent.getChatMsg().getUserId();
            //当 第一次连接的 时候 把 发送者的 id 和channel 关联起来 channel用来 返回聊天信息
            FrontUserChannelRel.put(userId, currentChannel);
            //用channel的当前id作为key  userId作为value存储起来
            FrontUserChannelRel.putUserId(currentChannel.id().asLongText(), userId);
        }
        // 2.2 action = 2  后端用户 当websocket 第一次open的时候，初始化channel，把用的channel和userid关联起来
        if (action == MessageEnum.MANAGER.getType()) {
            //客服的id
            String managerUserId = dataContent.getChatMsg().getUserId();
            // 存储一下 后端用户 的id 和对应的channel
            ManagerUserChannelRel.put(managerUserId, currentChannel);
            // 并且存储在线用户的id
            ManagerUserChannelRel.addUser(managerUserId);
            FrontUserChannelRel.putUserId(currentChannel.id().asLongText(), managerUserId);

            FrontUserChannelRel.outputFront();
        }
        // 3 前端给后端的用户发送消息
        if (action == MessageEnum.SEND_MANAGE.getType()) {
            // 4.查看后端用户是否在线
            if (ManagerUserChannelRel.users.size() != 0) {
                // 这个时候 说明有人是在线的状态
                // 随机匹配一个空闲的客服
                String users = ManagerUserChannelRel.getUsers();
                //这个时候就可以给这个客服发送消息了
                Channel channel = ManagerUserChannelRel.get(users);
                channel.writeAndFlush(
                        new TextWebSocketFrame(
                                "[前端在]" + LocalDateTime.now()
                                        + "发送消息, 消息为：" + dataContent.getChatMsg().getMsg()));
                //这个时候 把 这个客服 应该弄成 忙碌里面
            }
            //没有人在线的话 应该怎么处理??
            //将消息存储到数据库中
        }
        // 4.后端给前端的用户发送消息
        if (action == MessageEnum.SEND_FRONT.getType()) {
            //通过 签收人来发送
            String id = dataContent.getChatMsg().getReceiverId();
            //通过这个id来查找客户
            Channel channel = FrontUserChannelRel.get(id);
            channel.writeAndFlush(new TextWebSocketFrame(
                    "[后端在]" + LocalDateTime.now()
                            + "发送消息, 消息为：" + dataContent.getChatMsg().getMsg()));
        }

        // 6.还有一个优化 功能如果 客户端长时间不说话 连接断开
        else if (action == MessageEnum.KEEPALIVE.getType()) {
            //  2.4  心跳类型的消息
            System.out.println("收到来自channel为[" + currentChannel + "]的心跳包...");
        }

    }

    /**
     * 当客户端 连接的时候 进入当前方法
     *
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
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 5.如果用户断开后 应该怎么去 释放掉这个资源呢？
        // 由于关闭的时候 无法拿到 userId 去删除 存在内存的变量 现在改用 channel的id来作为key
        //根据channel的Id来获取userId
        String userId = FrontUserChannelRel.getUserId(ctx.channel().id().asLongText());
        //删除前端的用户对应的channel
        FrontUserChannelRel.remove(userId);
        //删除后端的用户对应的channel
        ManagerUserChannelRel.remove(userId);
        //删除list中后端的用户
        ManagerUserChannelRel.removeUserId(userId);
        String channelId = ctx.channel().id().asShortText();
        String longText = ctx.channel().id().asLongText();
        System.out.println("longText = " + longText);
        System.out.println("客户端被移除，channelId为：" + channelId);

        System.out.println("获取后端的channel" + ManagerUserChannelRel.manager.get("2"));
        System.out.println("后端在线的数量是:" + ManagerUserChannelRel.users.size());
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
