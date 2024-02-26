package com.hitsz.badboyChat.common.chat.service.mark;

import com.hitsz.badboyChat.common.chat.enums.MsgMarkTypeEnum;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/25 16:12
 */
public class DisLikeStrategy extends AbstractMsgMarkStrategy{
    @Override
    public MsgMarkTypeEnum getType() {
        return  MsgMarkTypeEnum.DISLIKE;
    }
}
