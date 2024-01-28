package com.hitsz.badboyChat.common.websocket.service;

import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/21 11:33
 */
public interface WebSocketService {
    /**
     * 建立连接
     * @param channel
     */
    void connect(Channel channel);

    /**
     *  处理用户登录请求
     * @param channel
     */
    void handleLoginReq(Channel channel) throws WxErrorException;

    /**
     * 移除用户与通道的绑定关系
     * @param channel
     */
    void offline(Channel channel);

    /**
     * 登陆成功后向用户推送消息
     * @param code
     * @param uid
     */
    void scanLoginSucess(Integer code, Long uid);

    /**
     * 等待授权给用户发送一条消息
     * @param code
     */
    void waitAuthroize(Integer code);


    /**
     *  登录认证
     * @param channel 通道
     * @param token token
     */
    void authroize(Channel channel, String token);
}
