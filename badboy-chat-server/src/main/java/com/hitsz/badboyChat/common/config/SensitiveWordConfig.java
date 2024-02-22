package com.hitsz.badboyChat.common.config;

import com.hitsz.badboyChat.common.service.factory.MyWordFactory;
import com.hitsz.badboyChat.common.utils.sensitiveword.DFAFilter;
import com.hitsz.badboyChat.common.utils.sensitiveword.SensitiveWordBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/22 21:05
 */
@Configuration
public class SensitiveWordConfig {

    @Autowired
    private MyWordFactory myWordFactory;

    @Bean
    public SensitiveWordBs getSensitiveWordBs(){
        return SensitiveWordBs.newInstance()
                .sensitiveWordBs(myWordFactory)
                .filterStrategy(DFAFilter.newInstance())
                .init();
    }

}
