package com.hitsz.badboyChat.common.chat.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/28 18:24
 */
@Data
public class AdminAddOrRemoveReq {
    @NotNull
    @ApiModelProperty("房间id")
    private Long roomId;

    @NotNull
    @Size(min = 1, max = 3)
    @ApiModelProperty("将要添加为管理员的用户id")
    private List<Long> uids;
}
