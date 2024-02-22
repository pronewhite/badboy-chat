package com.hitsz.badboyChat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/20 22:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssResp {

    @ApiModelProperty("文件下载地址")
    private String downLoadUrL;
}
