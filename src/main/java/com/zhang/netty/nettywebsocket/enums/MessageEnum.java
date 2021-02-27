package com.zhang.netty.nettywebsocket.enums;

import java.util.HashMap;

/**
 * @author ZHANGSHUAI
 * @version 1.0
 * @date 2021-02-27 16:37
 */
public enum MessageEnum {


    FRONT(1, "客户端第一次连接"),
    MANAGER(2, "后端系统第一次连接"),
    SEND_MANAGE(3, "客户端给后端发送消息"),
    SEND_FRONT(4, "后端给客户端发送消息"),
    KEEPALIVE(6, "保持心跳");

    public final Integer type;
    public final String content;

    MessageEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }


    public static void main(String[] args) {

//        int b = 10;
//        // 第一次不走 ; 后面
//        //  1 9-2 = 8
//
//        for (int a = 0; a < b; a++, b--) {
//            // 第一次 b 没有 --
//            System.out.println(b - a);
//        }


    }

}
