package com.hitsz.badboyChat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/7 13:46
 */
@Data
@ApiModel("好友信息")
public class FriendResp {

    @ApiModelProperty("好友ID")
    private Long uid;
    @ApiModelProperty("好友用户名")
    private String username;
    @ApiModelProperty("好友头像")
    private String avatar;
    /**
     * @see com.hitsz.badboyChat.common.enums.FriendActiveStatusEnum
     */
    @ApiModelProperty("在线状态")
    private Integer activeStatus;
    @ApiModelProperty("最后一次上下线时间")
    private Date lastOptionTime;
}
