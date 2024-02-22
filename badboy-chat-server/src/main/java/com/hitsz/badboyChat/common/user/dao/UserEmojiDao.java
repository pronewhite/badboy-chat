package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.UserEmoji;
import com.hitsz.badboyChat.common.user.domain.vo.resp.EmojiResp;
import com.hitsz.badboyChat.common.user.mapper.UserEmojiMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/21 13:30
 */
@Service
public class UserEmojiDao extends ServiceImpl<UserEmojiMapper, UserEmoji>{
    public List<EmojiResp> getUserEmojiList(Long uid) {
        return lambdaQuery()
                .eq(UserEmoji::getUid, uid)
                .list()
                .stream()
                .map(emoji -> {
                    return EmojiResp.builder()
                            .emojiUrl(emoji.getExpressionUrl())
                            .id(emoji.getId())
                            .build();
                }).collect(Collectors.toList());
    }

    public UserEmoji getUserEmojiById(Long uid, String emojiUrl) {
        return lambdaQuery()
                .eq(UserEmoji::getUid, uid)
                .eq(UserEmoji::getExpressionUrl,emojiUrl)
                .one();
    }

    public Integer getUserEmojiCount(Long uid) {
        return lambdaQuery()
                .eq(UserEmoji::getUid, uid)
                .count();
    }
}
