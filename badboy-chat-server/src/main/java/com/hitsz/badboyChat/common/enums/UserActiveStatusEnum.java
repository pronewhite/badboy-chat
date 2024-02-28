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
 * Create by 2024/2/3 19:57
 */
@AllArgsConstructor
@Getter
public enum UserActiveStatusEnum {

    ONLINE(1,"在线"),
    OFFLINE(2,"离线");

    private Integer status;
    private String desc;

    private static Map<Integer, UserActiveStatusEnum> cache;

    static {
        cache = Arrays.stream(UserActiveStatusEnum.values()).collect(Collectors.toMap(UserActiveStatusEnum::getStatus, Function.identity()));
    }

    public static UserActiveStatusEnum of(Integer code) {
        return cache.get(code);
    }
}
