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
public enum ApplyStatusEnum {

    WAIT_PROCESS(1,"待审批"),
    AGREE(2,"同意");

    private Integer code;
    private String desc;

    private static Map<Integer, ApplyStatusEnum> cache;

    static {
        cache = Arrays.stream(ApplyStatusEnum.values()).collect(Collectors.toMap(ApplyStatusEnum::getCode, Function.identity()));
    }

    public static ApplyStatusEnum of(Integer code) {
        return cache.get(code);
    }
}
