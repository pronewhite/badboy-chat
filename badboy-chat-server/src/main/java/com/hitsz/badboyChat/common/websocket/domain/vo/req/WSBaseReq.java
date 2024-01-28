package com.hitsz.badboyChat.common.websocket.domain.vo.req;

import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/20 16:46
 */
@Data
public class WSBaseReq {

    /**
     * @see com.hitsz.badboyChat.common.websocket.domain.enums.WSReqTypeEnum
     */
    private Integer type;

    private String data;
}
