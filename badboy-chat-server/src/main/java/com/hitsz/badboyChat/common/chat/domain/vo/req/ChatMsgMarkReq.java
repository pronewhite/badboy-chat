package com.hitsz.badboyChat.common.chat.domain.vo.req;

import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/25 17:31
 */
@Data
public class ChatMsgMarkReq {

    private Long msgId;

    private Integer actType;

    private Integer markType;

}
