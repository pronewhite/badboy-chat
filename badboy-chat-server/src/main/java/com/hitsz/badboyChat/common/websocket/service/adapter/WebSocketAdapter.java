package com.hitsz.badboyChat.common.websocket.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.hitsz.badboyChat.common.chat.domain.dto.ChatMsgRecallDTO;
import com.hitsz.badboyChat.common.chat.domain.dto.MsgRecall;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.websocket.domain.enums.WSRespTypeEnum;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSLoginUrl;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSMsgRecall;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/21 12:09
 */
public class WebSocketAdapter {


    public static WSBaseResp<WSLoginUrl> buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        return resp;
    }

    public static WSBaseResp<?> buildLoginSuccessResp(User user, String token, boolean hasPower) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        WSLoginSuccess build = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .uid(user.getId())
                .name(user.getName())
                .token(token)
                .power(hasPower ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode())
                .build();
        resp.setData(build);
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthResp() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }

    public static WSBaseResp<?> buildInvalidTokenResp() {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return resp;
    }

    public static WSBaseResp<?> buildPushMsg(ChatMessageResp chatMessageResp) {
        WSBaseResp<ChatMessageResp> resp = new WSBaseResp<>();
        resp.setData(chatMessageResp);
        resp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        return resp;
    }

    public static WSBaseResp<?> buildMsgRecall(ChatMsgRecallDTO chatMsgRecallDTO) {
        WSBaseResp<WSMsgRecall> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.RECALL.getType());
        WSMsgRecall msgRecall = new WSMsgRecall();
        BeanUtil.copyProperties(chatMsgRecallDTO, msgRecall);
        resp.setData(msgRecall);
        return resp;
    }
}
