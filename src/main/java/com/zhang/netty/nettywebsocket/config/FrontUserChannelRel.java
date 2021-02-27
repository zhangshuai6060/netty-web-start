package com.zhang.netty.nettywebsocket.config;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * 用户id和channel的关联关系处理
 *
 * @author ZHANGSHUAI
 * @version 1.0
 * @date 2021-02-27 14:26
 */
public class FrontUserChannelRel {

    /**
     * 前端用户
     */
    private static HashMap<String, Channel> front = new HashMap<>();

    /**
     * 用来存储 channel的id 和 userId
     */
    private static HashMap<String, String> frontChannel = new HashMap<>();

    /**
     * @param userId
     * @param channel
     */
    public static void put(String userId, Channel channel) {
        front.put(userId, channel);
    }

    /**
     * @param userId
     * @return
     */
    public static Channel get(String userId) {
        return front.get(userId);
    }

    public static void remove(String userId) {
        front.remove(userId);
    }

    /**
     * @param channelId
     * @param
     */
    public static void putUserId(String channelId, String userId) {
        frontChannel.put(channelId, userId);
    }

    /**
     * @param channelId
     * @return
     */
    public static String getUserId(String channelId) {
        return frontChannel.get(channelId);
    }

    /**
     * @param channelId
     * @return
     */
    public static void removeChannelId(String channelId) {
        frontChannel.remove(channelId);
    }

    /**
     *
     */
    public static void output() {
        for (HashMap.Entry<String, Channel> entry : front.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }

    /**
     *
     */
    public static void outputFront() {
        for (HashMap.Entry<String, String> entry : frontChannel.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue());
        }
    }

}
