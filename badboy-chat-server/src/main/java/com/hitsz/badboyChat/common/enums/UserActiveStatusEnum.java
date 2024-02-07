package com.hitsz.badboyChat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/3 19:57
 */
@AllArgsConstructor
@Getter
public enum UserActiveStatusEnum {

    ONLINE(1,"在线"),
    OFFLINE(2,"离线");

    private Integer status;
    private String desc;
}
