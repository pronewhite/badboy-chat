package com.hitsz.badboyChat.common.config;


import com.hitsz.badboyChat.common.thread.MyThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {
    /**
     * 项目共用线程池
     */
    public static final String BADBOYCHAT_EXECUTOR = "badboychatExecutor";
    /**
     * websocket通信线程池
     */
    public static final String WS_EXECUTOR = "websocketExecutor";

    @Override
    public Executor getAsyncExecutor() {
        return badboychatExecutor();
    }

    @Bean(BADBOYCHAT_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor badboychatExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        // 设置优雅停机
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("badboychat-executor-");
        // 自定义的ThreadFactory，设置了UncaughtExceptionHandler,将异常日志打印出来，方便排查错误
        executor.setThreadFactory(new MyThreadFactory(executor));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//满了调用线程执行，认为重要任务
        executor.initialize();
        return executor;
    }
}
