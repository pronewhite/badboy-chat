package com.hitsz.badboyChat.common.utils.sensitiveword;

import cn.hutool.core.collection.CollectionUtil;
import com.hitsz.badboyChat.common.exception.BusinessException;
import com.hitsz.badboyChat.common.service.factory.SensitiveWordFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/22 21:14
 */
@NoArgsConstructor
public class SensitiveWordBs {

    /**
     * 敏感词策略，默认为DFA算法
     */
    private SensitiveWordFilter sensitiveWordFilter = DFAFilter.newInstance();

    private SensitiveWordFactory wordFactory;

    public static SensitiveWordBs newInstance() {
        return new  SensitiveWordBs();
    }

    public SensitiveWordBs filterStrategy(SensitiveWordFilter wordFilter) {
        if(wordFilter == null){
            throw new BusinessException("敏感词过滤器不能为空");
        }
        this.sensitiveWordFilter = wordFilter;
        return this;
    }

    public SensitiveWordBs sensitiveWordBs(SensitiveWordFactory wordFactory) {
        if(wordFactory == null){
            throw new BusinessException("敏感词工厂不能为空");
        }
        this.wordFactory = wordFactory;
        return this;
    }

    public SensitiveWordBs init(){
        List<String> sensitiveWords = wordFactory.getSensitiveWords();
        loadSensitiveSord(sensitiveWords);
        return this;
    }

    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }
        return sensitiveWordFilter.filter(text);
    }

    public boolean hasSensitiveWord(String text){
        if(StringUtils.isBlank(text)){
            return false;
        }
        return sensitiveWordFilter.hasSensitiveWord(text);
    }

    public void loadSensitiveSord(List<String> sensitiveWords){
        if(CollectionUtil.isEmpty(sensitiveWords)){
            return;
        }
        sensitiveWordFilter.loadSensitiveWord(sensitiveWords);
    }

}
