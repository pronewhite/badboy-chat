package com.hitsz.badboyChat.common.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 16:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgRecall {

    private Long recallUid;

    private Date recallTime;
}
