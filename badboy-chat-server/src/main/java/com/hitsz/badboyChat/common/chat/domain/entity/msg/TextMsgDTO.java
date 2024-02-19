package com.hitsz.badboyChat.common.chat.domain.entity.msg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 15:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextMsgDTO {

    @ApiModelProperty("消息内容")
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 1024, message = "发送的消息不能太长")
    private String content;

    @ApiModelProperty("回复的消息id，如果该消息不是回复消息，则不用传")
    private Long replyId;

    @ApiModelProperty("艾特的用户id")
    @Size(max = 10, message = "最多只能艾特10个用户哦")
    private List<Long> replyUids;
}
