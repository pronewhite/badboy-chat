package com.hitsz.badboyChat.common.chat.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 16:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgReadInfoResp {
    @ApiModelProperty("消息id")
    private Long msgId;
    @ApiModelProperty("已读总数")
    private Integer readCount;
    @ApiModelProperty("未读总数")
    private Integer unReadCount;

}
