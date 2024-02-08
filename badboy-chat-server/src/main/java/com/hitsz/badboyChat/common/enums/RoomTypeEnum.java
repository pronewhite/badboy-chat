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
public enum RoomTypeEnum {

    GROUP_CHAT(1,"群聊"),
    SINGLE_CHAT(2,"单聊");

    private Integer code;
    private String desc;

    private static Map<Integer, RoomTypeEnum> cache;

    static {
        cache = Arrays.stream(RoomTypeEnum.values()).collect(Collectors.toMap(RoomTypeEnum::getCode, Function.identity()));
    }

    public static RoomTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
