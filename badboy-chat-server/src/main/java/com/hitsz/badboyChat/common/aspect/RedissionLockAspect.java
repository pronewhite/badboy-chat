package com.hitsz.badboyChat.common.aspect;

import com.hitsz.badboyChat.common.annotation.RedissionLock;
import com.hitsz.badboyChat.common.service.LockService;
import com.hitsz.badboyChat.common.utils.SpElUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/2 20:04
 */
@Slf4j
@Component
@Aspect
@Order(0)// 确保比事务注解先执行
public class RedissionLockAspect {

    @Autowired
    private LockService lockService;

    @Around("@annotation(redissionLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissionLock redissionLock){
        // 通过jointPoint 获取注解对应的Method
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String prefix = StringUtils.isBlank(redissionLock.prefix()) ? SpElUtil.getMethodKey(method) : redissionLock.prefix();
        String key = SpElUtil.parseSpEl(method, joinPoint.getArgs(), redissionLock.key());
        return lockService.executeWithLock(prefix + key, redissionLock.waitTime(), redissionLock.timeUnit(), joinPoint::proceed);
    }
}
