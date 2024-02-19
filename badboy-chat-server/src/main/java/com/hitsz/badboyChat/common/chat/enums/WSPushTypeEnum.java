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
public enum WSPushTypeEnum {

    PERSONAL(1,"个人"),
    GROUP_MEMBER(2,"全员");

    private Integer code;
    private String desc;

    private static Map<Integer, WSPushTypeEnum> cache;

    static {
        cache = Arrays.stream(WSPushTypeEnum.values()).collect(Collectors.toMap(WSPushTypeEnum::getCode, Function.identity()));
    }

    public static WSPushTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
