package com.hitsz.badboyChat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import java.util.Date;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/12 12:53
 */
@Data
@Builder
@AllArgsConstructor
public class SummeryInfoReq {

    @NonNull
    @Max(50)
    @ApiModelProperty("请求用户信息的列表")
    private List<Info> reqList;

    @Data
    public static class Info{
        private Long uid;
        private Long lastModifytime;
    }
}
