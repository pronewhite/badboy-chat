package com.hitsz.badboyChat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/4 13:37
 */
@Data
@ApiModel
public class BlackUserReq {

    @NonNull
    @ApiModelProperty("拉黑用户uid")
    private Long userId;
}
