package com.hitsz.badboyChat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/8 22:01
 */
@Data
public class FriendApplyResp {
    @ApiModelProperty("申请id")
    private Long applyId;
    @ApiModelProperty("申请人uid")
    private Long uid;
    @ApiModelProperty("申请类型 1加好友")
    private Integer type;
    @ApiModelProperty("申请信息")
    private String msg;
    @ApiModelProperty("申请状态 1待审批 2同意")
    private Integer status;
}
