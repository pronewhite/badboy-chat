package com.hitsz.badboychat.transaction.service;

import lombok.Data;

import java.util.Objects;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 16:24
 */
@Data
public class SecureInvokeHolder {

    private static ThreadLocal<Boolean> INVOKE_THREADLOCAL = new ThreadLocal<>();

    public static boolean isInvoking(){
        return Objects.nonNull(INVOKE_THREADLOCAL.get());
    }

    public static void setInvoke(){
        INVOKE_THREADLOCAL.set(true);
    }

    public static void invoked(){
        INVOKE_THREADLOCAL.remove();
    }
}
