package com.zhang.netty.nettywebsocket.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataContent implements Serializable {

    /**
     * 动作类型   1.前端第一次连接
     * 2.保持心跳
     * 3.后端第一次连接
     * 4.
     */
    private Integer action;

    /**
     * 用户的聊天内容entity
     */
    private ChatMsg chatMsg;

    /**
     * 扩展字段
     */
    private String extand;

}
