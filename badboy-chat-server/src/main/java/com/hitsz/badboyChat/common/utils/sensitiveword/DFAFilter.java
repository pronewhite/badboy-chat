package com.hitsz.badboyChat.common.utils.sensitiveword;

import cn.hutool.extra.tokenizer.Word;
import jodd.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/21 17:23
 */
@Component
public final class DFAFilter implements SensitiveWordFilter{

    private DFAFilter(){}

    // 初始化根节点
    private static Word root = new Word(' ');
    // 输入文本如果包含敏感词，则用*代替
    private static char replace = '*';
    // 遇到以下字符就会跳过
    private static String skipChars = " !*-+_=,，.@;:；：。、？?（）()【】[]《》<>“”\"‘’";
    private static Set<Character> skipSet = new HashSet<>();

    // 初始化skipSet
    static {
        for (char c : skipChars.toCharArray()){
            skipSet.add(c);
        }
    }
    @Override
    public String filter(String text) {
        if(StringUtil.isBlank(text)){
            return text;
        }
        StringBuilder result = new StringBuilder(text);
        int i = 0;
        while(i < text.length()){
            int start = i;
            // 如果是大写转为小写
            char c = text.charAt(i);
            Word word = root;
            if(Character.isUpperCase(c)){
                c = Character.toLowerCase(c);
            }
            if(skip(c)){
                i++;
                continue;
            }
            boolean fount = false;
            for(int j = i;j < text.length(); j++){
                c = text.charAt(j);
                // 大写转为小写
                if(Character.isUpperCase(c)){
                    c = Character.toLowerCase(c);
                }
                if(skip(c)){
                    j++;
                    continue;
                }
                 word = word.next.get(c);
                // 如果word为null，表示没有命中敏感词
                if(word == null){
                    break;
                }
                // 如果已经匹配完一个敏感词，那么表示输入的内容中包含敏感词，需要将敏感词部分替换为replace
                if(word.end){
                    fount = true;
                    for(int k = start;k <= j; k++){
                        // 将text下标为k的字符替换为replace
                        result.setCharAt(k, replace);
                    }
                    // 已经在输入的文本中找到了一个敏感词，下一次直接从当前位置的下一位置开始匹配即可
                    i = j + 1;
                }
            }
            if(!fount){
                i++;
            }
        }
        return result.toString();
    }

    @Override
    public boolean hasSensitiveWord(String text) {
        return !Objects.equals(text, filter(text));
    }

    @Override
    public void loadSensitiveWord(List<String> sensitiveWordList) {
        if(CollectionUtils.isEmpty(sensitiveWordList)){
            return;
        }
        Word newRoot = new Word(' ');
        sensitiveWordList.forEach(word -> loadSensitiveWord(word, newRoot));
        root = newRoot;
    }

    private void loadSensitiveWord(String word, Word root) {
        if(StringUtil.isBlank(word)){
            return;
        }
        Word current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            // 如果是大写字母, 转换为小写
            if (c >= 'A' && c <= 'Z') {
                c += 32;
            }
            // 如果当前字符是特殊字符，直接跳过
            if(skip(c)){
                continue;
            }
            Word next = current.next.get(c);
            if(next == null){
                next = new Word(c);
                current.next.put(c, next);
            }
            current = next;
        }
        current.end = true;
    }
    
    private boolean skip(char c){
        return skipSet.contains(c);
    }

    public static DFAFilter newInstance(){
        return new DFAFilter();
    }

    public static class Word{
        // 当前字符
        private char c;

        // 当前字符的下一层级的字符
        private Map<Character, Word> next;

        // 当前字符是否是最后一层
        private boolean end;

        private  Word(char c){
            this.c = c;
            this.next = new HashMap<>();
        }
    }
}
