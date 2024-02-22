package com.hitsz.badboyChat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/21 14:22
 */
@Data
@AllArgsConstructor
@Builder
public class EmojiDelReq {

    @ApiModelProperty("表情包id")
    private Long id;
}
