package com.hitsz.badboyChat.common.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lenovo
 * @version 1.0
 * Create by 2023/12/17 22:18
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissionConfig {

    // 主机
    private String host;
    // 端口号
    private String port;

    private String password;

    @Bean
    public RedissonClient redissonClient(){
        // 1. Create config object
        Config config = new Config();
        config.useSingleServer()
                // use "rediss://" for SSL connection
                .setAddress(String.format("redis://%s:%s", host, port))
//                .setPassword(password)
                .setDatabase(1);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
