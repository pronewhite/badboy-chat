package com.hitsz.badboyChat.common.user.service.impl;

import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import com.hitsz.badboyChat.common.user.service.UserService;
import com.hitsz.badboyChat.common.user.service.WXMsgService;
import com.hitsz.badboyChat.common.user.service.adapter.TextBuilder;
import com.hitsz.badboyChat.common.user.service.adapter.UserAdapter;
import com.hitsz.badboyChat.common.websocket.service.WebSocketService;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/21 16:26
 */
@Service
public class WXMsgServiceImpl implements WXMsgService {

    @Value("${wx.mp.callback}")
    private String callback;
    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    /**
     * 保存openId与登录码code的关系
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHROIZE_MAP = new ConcurrentHashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    @Lazy
    private WebSocketService webSocketService;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String openId = wxMpXmlMessage.getFromUser();
        // 这里封装的原因是：用户扫码登录和扫码关注时微信返回的eventKey是不同的，
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)) {
            return null;
        }
        // 通过openId 获取用户信息
        User user = userMapper.getUserByOpenId(openId);
        boolean registered = Objects.nonNull(user);
        boolean authorized = registered && user.getAvatar() != null;
        if (registered && authorized) {
            // 说明用户已经注册并且已经授权
            // 走登录成功流程
            webSocketService.scanLoginSucess(code, user.getId());
            return null;
        }
        if(!registered){
            // 用户未注册走注册流程
            User insert = UserAdapter.buildInsert(openId);
            userService.register(insert);
        }
        // 授权
        WAIT_AUTHROIZE_MAP.put(openId, code);
        // 等待授权期间需要向前端发送一条消息
        webSocketService.waitAuthroize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callback"));
        return TextBuilder.build("请点击授权:<a href=\"" + authorizeUrl  + "\" >登录</a>", wxMpXmlMessage);
    }

    @Override
    public void authroize(WxOAuth2UserInfo userInfo) {
        User user = userMapper.getUserByOpenId(userInfo.getOpenid());
        if(Objects.isNull(user)){
            return;
        }
        if(StringUtils.isBlank(user.getAvatar())){
            // 填充用户信息
            fillUserInfo( user,userInfo);
        }
        Integer code = WAIT_AUTHROIZE_MAP.remove(userInfo.getOpenid());
        // 向用户推送登录成功信息
        webSocketService.scanLoginSucess(code, user.getId());
    }

    private void fillUserInfo(User user, WxOAuth2UserInfo userInfo) {
        // 通过adapter来填充用户信息
        User updateUser = UserAdapter.buildFillUserInfo(user, userInfo);
        userMapper.updateById(updateUser);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        String eventKey = wxMpXmlMessage.getEventKey();
        String code = eventKey.replace("qrscene_", "");
        return Integer.parseInt(code);
    }
}
