package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/27 17:31
 */
@Data
public class RemoveUserReq {
    @NonNull
    @ApiModelProperty("聊天室id")
    private Long roomId;
    @ApiModelProperty("移除用户的id")
    private Long uid;
}
