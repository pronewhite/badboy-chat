package com.hitsz.badboyChat.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/25 21:29
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("线程{}发生异常，异常信息为：{}",t.getName(),e.getMessage());
    }
}
