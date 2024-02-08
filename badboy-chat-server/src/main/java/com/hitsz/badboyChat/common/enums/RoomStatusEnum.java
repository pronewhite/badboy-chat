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
public enum RoomStatusEnum {

    ABNORMAL(1,"禁用"),
    NORMAL(0,"正常");

    private Integer code;
    private String desc;
}
