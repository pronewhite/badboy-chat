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
public enum RoomHotFlagEnum {

    YES(1,"热点"),
    NO(0,"非热点");

    private Integer code;
    private String desc;

    private static Map<Integer, RoomHotFlagEnum> cache;

    static {
        cache = Arrays.stream(RoomHotFlagEnum.values()).collect(Collectors.toMap(RoomHotFlagEnum::getCode, Function.identity()));
    }

    public static RoomHotFlagEnum of(Integer code) {
        return cache.get(code);
    }
}
