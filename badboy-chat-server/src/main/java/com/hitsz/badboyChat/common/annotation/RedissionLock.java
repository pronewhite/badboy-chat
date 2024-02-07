package com.hitsz.badboyChat.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/2 17:27
 */
@Target(ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface RedissionLock {

    /**
     * key的前缀
     * @return
     */
    String prefix() default "";

    /**
     * SpEL表达式形式的key
     * @return
     */
    String key() default "";

    /**
     * 锁的过期时间
     * @return
     */
    int waitTime() default -1;

    /**
     * 过期时间的单位，默认是秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
