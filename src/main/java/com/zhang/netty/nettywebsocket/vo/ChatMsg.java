package com.zhang.netty.nettywebsocket.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMsg implements Serializable {

    /**
     * 发送者的用户id
     */
    private String userId;

    /**
     * 接受者的用户id
     */
    private String receiverId;

    /**
     * 聊天内容
     */
    private String msg;

    /**
     * 用于消息的签收
     */
    private String msgId;

}
