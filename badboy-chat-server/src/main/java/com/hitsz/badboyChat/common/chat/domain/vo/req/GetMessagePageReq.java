package com.hitsz.badboyChat.common.chat.domain.vo.req;

import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 21:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMessagePageReq extends CursorPageBaseReq {

    @ApiModelProperty("房间id")
    private Long roomId;

}
