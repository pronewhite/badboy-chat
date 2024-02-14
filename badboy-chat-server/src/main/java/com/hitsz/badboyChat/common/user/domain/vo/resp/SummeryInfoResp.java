package com.hitsz.badboyChat.common.user.domain.vo.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/12 13:05
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummeryInfoResp {

    @ApiModelProperty("用户昵称")
    private String name;
    @ApiModelProperty("用户头像")
    private String avatar;
    @ApiModelProperty("用户uid")
    private Long uid;
    @ApiModelProperty("是否需要刷新用户信息")
    private Boolean needRefresh = Boolean.TRUE;
    @ApiModelProperty("用户归属地")
    private String locPlace;
    @ApiModelProperty("用户佩戴的徽章id")
    private Long wearingItemId;
    @ApiModelProperty("用户拥有的徽章列表")
    private List<Long> itemList;

    public static SummeryInfoResp skip(Long uid) {
        return SummeryInfoResp.builder()
                .uid(uid)
                .needRefresh(Boolean.FALSE)
                .build();
    }

}
