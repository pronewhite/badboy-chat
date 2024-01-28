package com.hitsz.badboyChat.common.config;

import com.hitsz.badboyChat.common.interceptor.InfoCollectInterceptor;
import com.hitsz.badboyChat.common.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 16:37
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Autowired
    private InfoCollectInterceptor infoCollectInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/capi/**");
        registry.addInterceptor(infoCollectInterceptor)
                .addPathPatterns("/capi/**");
    }
}
