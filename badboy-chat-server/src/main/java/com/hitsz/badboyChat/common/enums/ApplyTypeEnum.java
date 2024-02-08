package com.hitsz.badboyChat.common.enums;

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
public enum ApplyTypeEnum {

    FRIEND(1,"添加好友");

    private Integer code;
    private String desc;

    private static Map<Integer, ApplyTypeEnum> cache;

    static {
        cache = Arrays.stream(ApplyTypeEnum.values()).collect(Collectors.toMap(ApplyTypeEnum::getCode, Function.identity()));
    }

    public static ApplyTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
