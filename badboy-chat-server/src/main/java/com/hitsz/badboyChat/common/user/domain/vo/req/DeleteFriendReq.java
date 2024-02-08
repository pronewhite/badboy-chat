package com.hitsz.badboyChat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 21:17
 */
@Data
public class DeleteFriendReq {

    @NonNull
    @ApiModelProperty("要删除的好友id")
    private Long friendId;
}
