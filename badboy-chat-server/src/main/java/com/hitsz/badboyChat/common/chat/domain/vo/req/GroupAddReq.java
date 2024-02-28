package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/28 17:27
 */
@Data
public class GroupAddReq {

    @ApiModelProperty("新建群组时邀请的uid")
    private List<Long> uids;

}
