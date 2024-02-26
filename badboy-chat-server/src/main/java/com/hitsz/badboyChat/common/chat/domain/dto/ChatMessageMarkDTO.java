package com.hitsz.badboyChat.common.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/25 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageMarkDTO {

    private Long uid;
    private Long msgId;
    private Integer markType;
    private Integer actType;
}
