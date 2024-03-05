package com.hitsz.badboyChat.common.chat.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 16:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileMsgDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件名（带后缀）")
    @NotBlank
    private String fileName;
}
