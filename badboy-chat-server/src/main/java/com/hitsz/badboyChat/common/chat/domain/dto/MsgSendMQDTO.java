package com.hitsz.badboyChat.common.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 16:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgSendMQDTO {
    private Long msgId;
}
