package com.hitsz.badboyChat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/28 20:39
 */
@AllArgsConstructor
@Getter
public enum RoomHotFlagEnum {

    YES(1,"全员展示"),
    NO(0,"不全员展示");

    private Integer code;
    private String desc;
}
