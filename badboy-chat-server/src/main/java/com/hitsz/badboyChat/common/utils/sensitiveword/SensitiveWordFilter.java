package com.hitsz.badboyChat.common.utils.sensitiveword;

import java.util.List;

public interface SensitiveWordFilter {

    /**
     * 根据敏感词过滤输入的文本，并返回脱敏后的文本
     * @param text 输入的文本
     * @return 脱敏后的文本
     */
    String filter(String text);

    /**
     * 判断输入的文本是否包含敏感词
     * @param text 输入的文本
     * @return true：包含敏感词，false：不包含敏感词
     */
    boolean hasSensitiveWord(String text);

    /**
     * 根据敏感词加载敏感词树
     * @param sensitiveWordList 敏感词
     */
    void loadSensitiveWord(List<String> sensitiveWordList);
}
