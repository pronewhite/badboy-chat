package com.hitsz.badboyChat.common.chat.domain.vo.resp;

import com.hitsz.badboyChat.common.chat.enums.UserRoomRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/26 20:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResp {
    private Long  roomId;
    private String name;
    private String avatar;
    private Integer onlineNumber;
    /**
     * @see UserRoomRoleEnum
     */
    private Integer role;
}
