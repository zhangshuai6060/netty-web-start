package com.zhang.netty.nettywebsocket.config;

import com.zhang.netty.nettywebsocket.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * spring容器加载完毕后,就会执行这个实现类的重写的方法.
 *
 * @author ZHANGSHUAI
 * @version 1.0
 * @date 2021-02-27 17:21
 */
@Component
public class NettyBoot implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //判断是不是 contextRefreshedEvent 已经加载完了 加载完了 加载我们的 server
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            try {
                WSServer.getInstance().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
