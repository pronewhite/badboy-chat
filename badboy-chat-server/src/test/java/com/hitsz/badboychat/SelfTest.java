package com.hitsz.badboychat;

import com.hitsz.badboyChat.common.user.utils.JwtUtils;
import com.hitsz.badboyChat.common.user.utils.RedisCommonProcessor;
import com.hitsz.badboyChat.common.utils.sensitiveword.DFAFilter;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private DFAFilter dfaFilter;

    @Test
    public void testDFA(){
        List<String> sensitiveWords =Arrays.asList("tmd", "nnd","ntmd");
        dfaFilter.loadSensitiveWord(sensitiveWords);
        System.out.println(dfaFilter.filter("tm,,d,nn....d"));
    }
    @Test
    public void sendMQ() {
        Message<String> build = MessageBuilder.withPayload("123").build();
        rocketMQTemplate.send("test-topic", build);
    }

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
