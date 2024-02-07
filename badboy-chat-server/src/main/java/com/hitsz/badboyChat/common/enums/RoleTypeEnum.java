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
public enum RoleTypeEnum {

    ADMIN(1,"超级管理员"),
    CHAT_MANAGER(2,"聊天室管理员");

    private Integer code;
    private String desc;

    private static Map<Integer, RoleTypeEnum> cache;

    static {
        cache = Arrays.stream(RoleTypeEnum.values()).collect(Collectors.toMap(RoleTypeEnum::getCode, Function.identity()));
    }

    public static RoleTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
