package com.hitsz.badboyChat.common.user.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/21 16:26
 */
public interface WXMsgService {
    /**
     * 处理微信扫码事件
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage);

    /**
     * 完成用户授权
     * @param userInfo
     */
    void authroize(WxOAuth2UserInfo userInfo);
}
