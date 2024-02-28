package com.hitsz.badboyChat.common.chat.domain.vo.req;

import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import lombok.Data;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/28 14:44
 */
@Data
public class MemberReq extends CursorPageBaseReq {
    private Long roomId;
}
