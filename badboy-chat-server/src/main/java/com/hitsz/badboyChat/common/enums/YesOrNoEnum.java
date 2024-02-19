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
public enum YesOrNoEnum {

    YES(1,"是"),
    NO(0,"否");

    private Integer code;
    private String desc;

    public static Integer getStatus(boolean condition) {
        return condition ? YES.getCode() : NO.getCode();
    }
}
