package com.hitsz.badboychat.transaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//运行时生效
@Target(ElementType.METHOD)//作用在方法上
public @interface SecureInvoke {

    /**
     * 最大重试次数，默认是3
     * @return
     */
    int maxRetryTimes() default 3;

    /**
     * 默认是异步执行，入库之后异步执行，提高效率。事务提交之后再串行执行意义不大，
     * @return
     */
    boolean async() default true;
}
