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
public enum MessageStatusEnum {

    NORAML(1,"正常文本消息"),
    DELETE(2,"删除消息");

    private Integer code;
    private String desc;

    private static Map<Integer, MessageStatusEnum> cache;

    static {
        cache = Arrays.stream(MessageStatusEnum.values()).collect(Collectors.toMap(MessageStatusEnum::getCode, Function.identity()));
    }

    public static MessageStatusEnum of(Integer code) {
        return cache.get(code);
    }
}
