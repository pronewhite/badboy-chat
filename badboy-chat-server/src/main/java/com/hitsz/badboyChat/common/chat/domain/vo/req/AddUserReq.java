package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/27 18:19
 */
@Data
public class AddUserReq {

    @NonNull
    @ApiModelProperty("聊天室id")
    private Long roomId;
    @ApiModelProperty("移除用户的id")
    private List<Long> uid;

}
