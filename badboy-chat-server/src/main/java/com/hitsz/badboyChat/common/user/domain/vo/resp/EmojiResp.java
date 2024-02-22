package com.hitsz.badboyChat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/21 13:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("表情包返回体")
public class EmojiResp {

    @ApiModelProperty("表情包id")
    private Long id;

    @ApiModelProperty("表情包地址")
    private String emojiUrl;
}
