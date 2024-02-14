package com.hitsz.badboychat.transaction.service;

import com.hitsz.badboychat.transaction.annotation.SecureInvoke;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.checkerframework.framework.qual.CFComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 23:17
 */
public class MQProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMsg(String topic, Object body) {
        Message<Object> build = MessageBuilder.withPayload(body).build();
        rocketMQTemplate.send(topic, build);
    }

    /**
     * 发送可靠消息，在事务提交后保证发送成功
     *
     * @param topic
     * @param body
     */
    @SecureInvoke
    public void sendSecureMsg(String topic, Object body, Object key) {
        Message<Object> build = MessageBuilder
                .withPayload(body)
                .setHeader("KEYS", key)
                .build();
        rocketMQTemplate.send(topic, build);
    }
}
