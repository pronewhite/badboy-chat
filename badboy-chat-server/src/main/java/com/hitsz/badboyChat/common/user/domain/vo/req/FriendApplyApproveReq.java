package com.hitsz.badboyChat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 12:31
 */
@Data
public class FriendApplyApproveReq {

    @NonNull
    @ApiModelProperty("申请id")
    private Long applyId;
}
