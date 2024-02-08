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
public enum ApplyReadStatusEnum {

    NOT_READ(1,"未读"),
    HAVE_READ(2,"已读");

    private Integer code;
    private String desc;

    private static Map<Integer, ApplyReadStatusEnum> cache;

    static {
        cache = Arrays.stream(ApplyReadStatusEnum.values()).collect(Collectors.toMap(ApplyReadStatusEnum::getCode, Function.identity()));
    }

    public static ApplyReadStatusEnum of(Integer code) {
        return cache.get(code);
    }
}
