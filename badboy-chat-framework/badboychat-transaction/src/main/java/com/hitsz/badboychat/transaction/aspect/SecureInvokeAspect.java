package com.hitsz.badboychat.transaction.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.hitsz.badboychat.transaction.annotation.SecureInvoke;
import com.hitsz.badboychat.transaction.dao.SecureInvokeRecordDao;
import com.hitsz.badboychat.transaction.domain.dto.SecureInvokeDTO;
import com.hitsz.badboychat.transaction.domain.entity.SecureInvokeRecord;
import com.hitsz.badboychat.transaction.service.SecureInvokeHolder;
import com.hitsz.badboychat.transaction.service.SecureInvokeService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 20:41
 */
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)//确保最先执行
@Component
public class SecureInvokeAspect {

    @Autowired
    private SecureInvokeRecordDao secureInvokeRecordDao;
    @Autowired
    private SecureInvokeService secureInvokeService;

    @Around("@annotation(secureInvoke)")
    public Object around(ProceedingJoinPoint joinPoint, SecureInvoke secureInvoke) throws Throwable{
        boolean async = secureInvoke.async();
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();

        if(SecureInvokeHolder.isInvoking() || !inTransaction){
            return joinPoint.proceed();
        }
        // 取出标有secureInvoke注解的方法的Method对象
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SecureInvokeDTO secureInvokeDTO = SecureInvokeDTO.builder()
                .className(joinPoint.getTarget().getClass().getName())
                .methodName(method.getName())
                .parametersType(JSONUtil.toJsonStr(method.getParameterTypes()))
                .args(JSONUtil.toJsonStr(joinPoint.getArgs()))
                .build();
        // 组装record
        SecureInvokeRecord secureInvokeRecord = SecureInvokeRecord.builder()
                .secureInvokeDTO(secureInvokeDTO)
                .maxRetryTimes(secureInvoke.maxRetryTimes())
                .nextRetryTime(DateUtil.offsetDay(new Date(), (int)SecureInvokeService.RETRY_DELAY_TIME))
                .build();
        secureInvokeService.invoke(secureInvokeRecord, async);
        return null;
    }
}
