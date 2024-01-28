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
 * Create by 2024/1/28 17:11
 */
@Data
@ApiModel(description = "用户信息响应体")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResp {

    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "用户名称")
    private String name;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "用户性别")
    private Integer sex;
    @ApiModelProperty(value = "用户剩余改名次数")
    private Integer modifyNameChance;
}
