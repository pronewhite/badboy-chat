package com.hitsz.badboyChat.common.websocket.domain.vo.resp;

import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/20 16:54
 */
@Data
public class WSBaseResp<T> {

    /**
     * @see com.hitsz.badboyChat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;

    private T data;
}
