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
public enum MsgMarkTypeEnum {

    LIKE(1,"点赞", 10),
    DISLIKE(2,"点踩", 5 );

    private Integer code;
    private String desc;
    private Integer count;

    private static Map<Integer, MsgMarkTypeEnum> cache;

    static {
        cache = Arrays.stream(MsgMarkTypeEnum.values()).collect(Collectors.toMap(MsgMarkTypeEnum::getCode, Function.identity()));
    }

    public static MsgMarkTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
