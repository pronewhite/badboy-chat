package com.hitsz.badboychat.transaction.domain.enums;

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
public enum SecureInvokeRecordStatusEnum {

    WAIT_PROCESS(1,"待执行"),
    FAIL(2,"已失败");

    private Integer code;
    private String desc;

    private static Map<Integer, SecureInvokeRecordStatusEnum> cache;

    static {
        cache = Arrays.stream(SecureInvokeRecordStatusEnum.values()).collect(Collectors.toMap(SecureInvokeRecordStatusEnum::getCode, Function.identity()));
    }

    public static SecureInvokeRecordStatusEnum of(Integer code) {
        return cache.get(code);
    }
}
