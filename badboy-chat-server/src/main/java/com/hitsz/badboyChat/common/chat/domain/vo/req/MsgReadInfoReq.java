package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 17:44
 */
@Data
public class MsgReadInfoReq {

    @ApiModelProperty("消息id列表")
    private List<Long> msgIds;
}
