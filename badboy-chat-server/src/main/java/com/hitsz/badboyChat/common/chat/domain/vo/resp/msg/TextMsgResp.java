package com.hitsz.badboyChat.common.chat.domain.vo.resp.msg;

import com.hitsz.badboyChat.common.chat.domain.dto.UrlInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 15:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextMsgResp {

    @ApiModelProperty("消息内容")
    private String content;
    @ApiModelProperty("艾特的用户")
    private List<Long> replyUidList;
    @ApiModelProperty("消息链接映射")
    private Map<String, UrlInfo> urlContentMap;
    @ApiModelProperty("回复的消息信息")
    private TextMsgResp.ReplyMsg reply;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReplyMsg{
        @ApiModelProperty("消息id")
        private Long id;
        @ApiModelProperty("回复的用户id")
        private Long uid;
        @ApiModelProperty("回复的用户名")
        private String userName;
        @ApiModelProperty("与回复消息的条数间隔")
        private Integer gapCount;
        @ApiModelProperty("消息类型：1，正常消息，2，撤回消息")
        private Integer type;
        @ApiModelProperty("消息内容，不同的消息类型对应不同的消息内容")
        private Object body;
        @ApiModelProperty("是否可跳转，1：可跳转，0：不可跳转")
        private Integer canCallback;

    }

}
