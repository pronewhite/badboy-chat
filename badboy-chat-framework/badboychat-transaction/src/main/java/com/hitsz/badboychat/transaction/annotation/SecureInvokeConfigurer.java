package com.hitsz.badboychat.transaction.annotation;

import org.springframework.lang.Nullable;

import java.util.concurrent.Executor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 23:13
 */
public interface SecureInvokeConfigurer {

    /**
     * 返回一个线程池
     */
    @Nullable
    default Executor getSecureInvokeExecutor() {
        return null;
    }

}
