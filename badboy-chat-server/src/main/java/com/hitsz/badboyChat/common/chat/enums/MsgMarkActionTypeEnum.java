package com.hitsz.badboyChat.common.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/28 20:39
 */
@AllArgsConstructor
@Getter
public enum MsgMarkActionTypeEnum {

    MARK(1,"确认标记"),
    CANCLE_MARK(2,"取消标记");

    private Integer code;
    private String desc;

    private static Map<Integer, MsgMarkActionTypeEnum> cache;

    static {
        cache = Arrays.stream(MsgMarkActionTypeEnum.values()).collect(Collectors.toMap(MsgMarkActionTypeEnum::getCode, Function.identity()));
    }

    public static MsgMarkActionTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
