package com.hitsz.badboyChat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdempotentEnum {

    UID(1,"uid"),
    MSG_ID(2, "消息id");

    public Integer type;
    public String desc;
}
