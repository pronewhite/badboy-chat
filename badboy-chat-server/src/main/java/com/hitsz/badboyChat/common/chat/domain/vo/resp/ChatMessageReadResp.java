package com.hitsz.badboyChat.common.chat.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 18:18
 */
@Data
public class ChatMessageReadResp {

    @ApiModelProperty("用户id")
    private Long uid;
}
