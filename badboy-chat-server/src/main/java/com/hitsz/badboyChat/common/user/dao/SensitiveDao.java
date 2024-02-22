package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.SensitiveWord;
import com.hitsz.badboyChat.common.user.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/21 17:16
 */
@Service
public class SensitiveDao extends ServiceImpl<SensitiveWordMapper, SensitiveWord>{
    public List<String> getSensitiveWords() {
        return lambdaQuery()
                .list()
                .stream()
                .map(SensitiveWord::getWord)
                .collect(Collectors.toList());
    }
}
