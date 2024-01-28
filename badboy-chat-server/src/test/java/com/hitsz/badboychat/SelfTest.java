package com.hitsz.badboychat;

import com.hitsz.badboyChat.common.user.utils.JwtUtils;
import com.hitsz.badboyChat.common.user.utils.RedisCommonProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/23 19:15
 */
@SpringBootTest
//告诉 Junit 使用 Spring 提供的测试运行器。
@RunWith(SpringRunner.class)
public class SelfTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void testJwt(){
        String token = jwtUtils.createToken(11000L);
        System.out.println(token);
    }

    @Test
    public void testRedis(){
        redisCommonProcessor.set("test","test");
        System.out.println(redisCommonProcessor.get("test"));
    }

    @Test
    public void testRedission(){
        // 测试redission
        RLock lock = redissonClient.getLock("badboy");
        lock.lock();
        lock.unlock();
    }

    @Test
    public void testThreadPoolTaskExecutor(){
        threadPoolTaskExecutor.execute(() -> {
            throw new RuntimeException("11111");
        });
    }
}
