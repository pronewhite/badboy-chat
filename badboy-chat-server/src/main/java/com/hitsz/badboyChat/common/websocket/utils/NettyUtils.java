package com.hitsz.badboyChat.common.websocket.utils;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/26 21:14
 */
public class NettyUtils {

    public static final AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static final AttributeKey<String> IP = AttributeKey.valueOf("ip");
    public static final AttributeKey<Long> UID = AttributeKey.valueOf("uid");

    // 向netty的channel中写数据
    public static <T> void setAttr(Channel channel, AttributeKey<T> key, T value){
        channel.attr(key).set(value);
    }

    // 向netty的channel中读数据
    public static <T> T getAttr(Channel channel, AttributeKey<T> key){
        return channel.attr(key).get();
    }

}
