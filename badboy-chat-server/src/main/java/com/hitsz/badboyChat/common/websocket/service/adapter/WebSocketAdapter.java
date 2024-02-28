package com.hitsz.badboyChat.common.websocket.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.hitsz.badboyChat.common.chat.domain.dto.ChatMessageMarkDTO;
import com.hitsz.badboyChat.common.chat.domain.dto.ChatMsgRecallDTO;
import com.hitsz.badboyChat.common.chat.domain.dto.MsgRecall;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMemberStatisticResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.ChatMessageResp;
import com.hitsz.badboyChat.common.chat.enums.MemberChangeTypeEnum;
import com.hitsz.badboyChat.common.chat.service.ChatService;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.websocket.domain.enums.WSRespTypeEnum;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.*;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/21 12:09
 */
@Component
public class WebSocketAdapter {

    @Autowired
    private ChatService chatService;


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

    public static WSBaseResp<?> buildMsgMarkPushMsg(ChatMessageMarkDTO dto, Integer msgMarkCount) {
        WSMsgMark.WSMsgMarkItem item = new WSMsgMark.WSMsgMarkItem();
        BeanUtils.copyProperties(dto, item);
        item.setMarkCount(msgMarkCount);
        WSBaseResp<WSMsgMark> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MARK.getType());
        WSMsgMark mark = new WSMsgMark();
        mark.setMarkList(Collections.singletonList(item));
        wsBaseResp.setData(mark);
        return wsBaseResp;
    }

    public static WSBaseResp<WSMemberChange> buildMemberChangeResp(Long roomId, GroupMember groupMember) {
        WSBaseResp<WSMemberChange> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        WSMemberChange wsMemberChange = new WSMemberChange();
        wsMemberChange.setRoomId(roomId);
        wsMemberChange.setUid(groupMember.getUid());
        wsMemberChange.setChangeType(MemberChangeTypeEnum.REMOVE.getCode());
        resp.setData(wsMemberChange);
        return resp;
    }

    public static WSBaseResp<WSMemberChange> buildAddUserPush(User user, Long roomId) {
        WSBaseResp<WSMemberChange> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        WSMemberChange wsMemberChange = new WSMemberChange();
        wsMemberChange.setActiveStatus(user.getActiveStatus());
        wsMemberChange.setUid(user.getId());
        wsMemberChange.setChangeType(WSMemberChange.CHANGE_TYPE_ADD);
        wsMemberChange.setRoomId(roomId);
        wsMemberChange.setLastOptTime(user.getLastOptTime());
        resp.setData(wsMemberChange);
        return resp;
    }

    public  WSBaseResp<WSOnlineOfflineNotify> buildOnlineResp(User user) {
        WSBaseResp<WSOnlineOfflineNotify> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.ONLINE_OFFLINE_NOTIFY.getType());
        WSOnlineOfflineNotify onlineOfflineNotify = new WSOnlineOfflineNotify();
        onlineOfflineNotify.setChangeList(Collections.singletonList(buildOnlineInfo(user)));
        assembleNum(onlineOfflineNotify);
        resp.setData(onlineOfflineNotify);
        return resp;
    }

    private  void assembleNum(WSOnlineOfflineNotify onlineOfflineNotify) {
        ChatMemberStatisticResp memberStatistic = chatService.getMemberStatistic();
        onlineOfflineNotify.setOnlineNum(memberStatistic.getOnlineNumber().longValue());// 组装在线人数
    }

    private static ChatMemberResp buildOnlineInfo(User user) {
        ChatMemberResp resp = new ChatMemberResp();
        resp.setUid(user.getId());
        resp.setActiveStatus(user.getActiveStatus());
        resp.setLastOptTime(user.getLastOptTime());
        return resp;
    }
}
