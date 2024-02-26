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
public enum UserRoomRoleEnum {

    LEADER(1,"群主"),
    ADMIN(2,"管理员"),
    USER(3,"普通成员"),
    REMOVED(4,"被移除群聊");

    private Integer code;
    private String desc;

    private static Map<Integer, UserRoomRoleEnum> cache;

    static {
        cache = Arrays.stream(UserRoomRoleEnum.values()).collect(Collectors.toMap(UserRoomRoleEnum::getCode, Function.identity()));
    }

    public static UserRoomRoleEnum of(Integer code) {
        return cache.get(code);
    }
}
