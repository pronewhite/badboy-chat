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
public enum MessageMarkTypeEnum {

    LIKE(1,"点赞"),
    DISLIKE(2,"举报");

    private Integer code;
    private String desc;

    private static Map<Integer, MessageMarkTypeEnum> cache;

    static {
        cache = Arrays.stream(MessageMarkTypeEnum.values()).collect(Collectors.toMap(MessageMarkTypeEnum::getCode, Function.identity()));
    }

    public static MessageMarkTypeEnum of(Integer code) {
        return cache.get(code);
    }
}
