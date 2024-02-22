package com.hitsz.badboyChat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/21 13:52
 */
@Data
@AllArgsConstructor
@Builder
public class EmojiAddReq {

    @ApiModelProperty("添加的表情包的地址")
    @NonNull
    private String emojiUrl;

}
