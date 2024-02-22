package com.hitsz.badboyChat.common.utils.sensitiveword;

import cn.hutool.core.collection.CollectionUtil;
import com.hitsz.badboyChat.common.utils.sensitiveword.ac.ACTree;
import com.hitsz.badboyChat.common.utils.sensitiveword.ac.ACTreeNode;
import com.hitsz.badboyChat.common.utils.sensitiveword.ac.MatchResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/22 14:40
 */
public class ACFilter implements SensitiveWordFilter{

    private static final char replace = '*';

    private ACTree acTree = null;

    @Override
    public String filter(String text) {
        if(StringUtils.isBlank(text)){
            return text;
        }
        List<MatchResult> match = acTree.match(text);
        StringBuilder result = new StringBuilder(text);
        int endIndex = 0;
        for (MatchResult matchResult : match) {
            endIndex = Math.max(endIndex, matchResult.getEndIndex());
            replaceTextBetween(result, matchResult.getStartIndex(), endIndex);
        }
        return result.toString();
    }

    private void replaceTextBetween(StringBuilder result, Integer startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++){
            result.setCharAt(i, replace);
        }
    }

    @Override
    public boolean hasSensitiveWord(String text) {
        if(StringUtils.isBlank(text)){
            return false;
        }
        return !Objects.equals(text, filter(text));
    }

    @Override
    public void loadSensitiveWord(List<String> sensitiveWordList) {
        if(CollectionUtil.isEmpty(sensitiveWordList)){
            return;
        }
        acTree = new ACTree(sensitiveWordList);
    }
}
