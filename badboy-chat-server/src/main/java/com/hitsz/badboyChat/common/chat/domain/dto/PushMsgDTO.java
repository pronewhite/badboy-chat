package com.hitsz.badboyChat.common.chat.domain.dto;

import com.hitsz.badboyChat.common.chat.enums.WSPushTypeEnum;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 22:18
 * 推送的消息的相关信息，比如，消息的内容，推送的用户id等
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushMsgDTO {
    /**
     * 推送的用户id
     */
    private List<Long> uidList;

    /**
     * 推送的webSocket消息
     */
    private WSBaseResp<?> wsBaseResp;

    /**
     * 推送类型，1：个人，2：全员
     *
     * @see com.hitsz.badboyChat.common.chat.enums.WSPushTypeEnum
     */
    private Integer pushType;

    public PushMsgDTO(WSBaseResp<?> wsBaseResp) {
        this.wsBaseResp = wsBaseResp;
        this.pushType = WSPushTypeEnum.GROUP_MEMBER.getCode();
    }

    public PushMsgDTO( WSBaseResp<?> wsBaseResp,List<Long> uidList) {
        this.uidList = uidList;
        this.wsBaseResp = wsBaseResp;
        this.pushType = WSPushTypeEnum.PERSONAL.getCode();
    }

    public PushMsgDTO(List<Long> uidList){
        this.uidList = uidList;
        this.pushType = WSPushTypeEnum.PERSONAL.getCode();
    }
}


