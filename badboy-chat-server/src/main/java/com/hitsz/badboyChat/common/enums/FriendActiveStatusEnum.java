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
public enum FriendActiveStatusEnum {

    ACTIVE_STATUS(1,"在线"),
    INACTIVE_STATUS(2,"离线");

    private Integer code;
    private String desc;

    private static Map<Integer, FriendActiveStatusEnum> cache;

    static {
        cache = Arrays.stream(FriendActiveStatusEnum.values()).collect(Collectors.toMap(FriendActiveStatusEnum::getCode, Function.identity()));
    }

    public static FriendActiveStatusEnum of(Integer code) {
        return cache.get(code);
    }
}
