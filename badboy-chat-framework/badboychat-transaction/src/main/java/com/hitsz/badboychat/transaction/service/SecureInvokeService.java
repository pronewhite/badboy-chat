package com.hitsz.badboychat.transaction.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.hitsz.badboychat.transaction.dao.SecureInvokeRecordDao;
import com.hitsz.badboychat.transaction.domain.dto.SecureInvokeDTO;
import com.hitsz.badboychat.transaction.domain.entity.SecureInvokeRecord;
import com.hitsz.badboychat.transaction.domain.enums.SecureInvokeRecordStatusEnum;
import com.hitsz.badboychat.transaction.utils.JsonUtils;
import io.github.classgraph.json.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 21:14
 */
@Service
@Slf4j
@AllArgsConstructor
public class SecureInvokeService {

    private SecureInvokeRecordDao secureInvokeRecordDao;

    public static final double RETRY_DELAY_TIME = 2D;

    private Executor executor;

    @Scheduled(cron = "*/5 * * * * ?")
    public void retry() {
        List<SecureInvokeRecord> secureInvokeRecords = secureInvokeRecordDao.getWaitRetryRecords();
        for (SecureInvokeRecord secureInvokeRecord : secureInvokeRecords) {
            doAsyncInvoke(secureInvokeRecord);
        }
    }


    public void invoke(SecureInvokeRecord secureInvokeRecord, boolean async) {
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if(!inTransaction){
            return;
        }
        // 入库
        save(secureInvokeRecord);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if(async){
                    doAsyncInvoke(secureInvokeRecord);
                }else{
                    doInvoke(secureInvokeRecord);
                }
            }
        });
    }

    private void doInvoke(SecureInvokeRecord secureInvokeRecord) {
        SecureInvokeDTO secureInvokeDTO = secureInvokeRecord.getSecureInvokeDTO();
        // 反射调用方法
        try {
            Class<?> className = Class.forName(secureInvokeDTO.getClassName());
            Object bean = SpringUtil.getBean(className);
            List<String> parameterTypes = JSONUtil.toList(secureInvokeDTO.getParametersType(), String.class);
            List<Class<?>> parameterTypesClass = getParamters(parameterTypes);
            // 拿出Method
            Method method = ReflectUtil.getMethod(className, secureInvokeDTO.getMethodName(), parameterTypesClass.toArray(new Class[]{}));
            // 调用方法
            Object[] args = getArgs(secureInvokeDTO.getArgs(), parameterTypesClass);
            // 执行方法
            method.invoke(bean, args);
            // 更新record
            refreshRecord(secureInvokeRecord);
        }catch (Exception e){
            log.error("invoke method error", e);
            // 重试
            retryInvoke(secureInvokeRecord, e.getMessage());
        }
    }

    private void retryInvoke(SecureInvokeRecord secureInvokeRecord, String message) {
        // 将record重试次数加一
        Integer retryTimes = secureInvokeRecord.getRetryTimes() + 1;
        // 设置错误信息
        secureInvokeRecord.setFailReason(message);
        // 设置下次更新时间
        secureInvokeRecord.setNextRetryTime(getNextRetryTime(retryTimes));
        // 如果重试次数大于最大重试次数，则将状态设置为失败
        if (retryTimes > secureInvokeRecord.getMaxRetryTimes()) {
            secureInvokeRecord.setStatus(SecureInvokeRecordStatusEnum.FAIL.getCode());
        }else{
            secureInvokeRecord.setRetryTimes(retryTimes);
        }
        secureInvokeRecordDao.updateById(secureInvokeRecord);
    }

    private Date getNextRetryTime(Integer retryTimes) {
        double waitTime = Math.pow(RETRY_DELAY_TIME, retryTimes);// 随着重试次数的增加，等待时间也随着增加
        return DateUtil.offsetMinute(new Date(), (int) waitTime);
    }

    private void refreshRecord(SecureInvokeRecord secureInvokeRecord) {
        secureInvokeRecordDao.removeById(secureInvokeRecord.getId());
    }

    private Object[] getArgs(String args, List<Class<?>> parameterTypesClass) {
        JsonNode jsonNode = JsonUtils.toJsonNode(args);
        Object[] argArrays = new Object[jsonNode.size()];
        for(int i = 1; i <= jsonNode.size(); i++){
            Class<?> aClass = parameterTypesClass.get(i);
            Object arg = JsonUtils.nodeToValue(jsonNode.get(i), aClass);
            argArrays[i] = arg;
        }
        return argArrays;
    }

    private List<Class<?>> getParamters(List<String> parameterTypes) {
        return parameterTypes.stream().map(name -> {
            try{
                return Class.forName(name);
            }catch (Exception e){
                log.error("获取参数类型失败", e);
            }
            return null;
        }).collect(Collectors.toList());

    }

    private void doAsyncInvoke(SecureInvokeRecord secureInvokeRecord) {
        executor.execute(() -> doInvoke(secureInvokeRecord));
    }

    private void save(SecureInvokeRecord secureInvokeRecord) {
        secureInvokeRecordDao.save(secureInvokeRecord);
    }
}
