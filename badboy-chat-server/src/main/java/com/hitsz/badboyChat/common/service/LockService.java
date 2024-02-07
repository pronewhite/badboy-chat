package com.hitsz.badboyChat.common.service;

import com.hitsz.badboyChat.common.enums.CommonErrorEnum;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LockService {

    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows
    public <T> T executeWithLock(String lockKey, int waitTime, TimeUnit timeUnit, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean sucess = lock.tryLock(waitTime, timeUnit);
        AssertUtil.isTrue(sucess, CommonErrorEnum.LOCK_LIMIT);
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        return executeWithLock(lockKey, -1, TimeUnit.SECONDS, supplier);
    }

    @FunctionalInterface
    public interface Supplier<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Throwable;
    }
}
