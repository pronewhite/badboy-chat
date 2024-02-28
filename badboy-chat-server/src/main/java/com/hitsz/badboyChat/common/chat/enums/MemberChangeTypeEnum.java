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
public enum MemberChangeTypeEnum {

    ADD(1,"加入群组"),
    REMOVE(2,"移除群组");

    private Integer code;
    private String desc;

    private static Map<Integer, MemberChangeTypeEnum> cache;

    static {
        cache = Arrays.stream(MemberChangeTypeEnum.values()).collect(Collectors.toMap(MemberChangeTypeEnum::getCode, Function.identity()));
    }

    public static MemberChangeTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
