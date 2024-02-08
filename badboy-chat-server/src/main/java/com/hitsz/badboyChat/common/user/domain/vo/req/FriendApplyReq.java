package com.hitsz.badboyChat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.units.qual.A;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 11:44
 */
@Data
@ApiModel("好友申请请求")
public class FriendApplyReq {

    @NonNull
    @ApiModelProperty("申请消息")
    private String msg;

    @NonNull
    @ApiModelProperty("目标id")
    private Long targetUid;
}
