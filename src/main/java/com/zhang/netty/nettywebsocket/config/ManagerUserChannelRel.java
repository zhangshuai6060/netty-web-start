package com.zhang.netty.nettywebsocket.config;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZHANGSHUAI
 * @version 1.0
 * @date 2021-02-27 15:30
 */
public class ManagerUserChannelRel {

    /**
     * 后端用户
     */
    public static HashMap<String, Channel> manager = new HashMap<>();

    /**
     * 正在忙碌的人 (暂时未使用到)
     */
    public static HashMap<String, Channel> now = new HashMap<>();

    /**
     * 后端用户在线的ids
     */
    public static List<String> users = new ArrayList<>();

    /**
     * @param userId
     * @param channel
     */
    public static void put(String userId, Channel channel) {
        manager.put(userId, channel);
    }

    /**
     * @param userId
     * @return
     */
    public static Channel get(String userId) {
        return manager.get(userId);
    }


    public static void remove(String userId) {
        manager.remove(userId);
    }

    public static void output() {
        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }

    /**
     * @param userId
     * @param channel
     */
    public static void putNow(String userId, Channel channel) {
        now.put(userId, channel);
    }

    /**
     * @param userId
     * @return
     */
    public static Channel getNow(String userId) {
        return now.get(userId);
    }

    public static void removeNow(String userId, Channel channel) {
        now.remove(userId);
    }

    public static void addUser(String userId) {
        users.add(userId);
    }

    public static String getUsers() {
        return users.get(0);
    }

    public static void removeUserId(String userId) {
        users.remove(userId);
    }
}
