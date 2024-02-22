package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.annotation.RedissionLock;
import com.hitsz.badboyChat.common.user.dao.UserEmojiDao;
import com.hitsz.badboyChat.common.user.domain.entity.UserEmoji;
import com.hitsz.badboyChat.common.user.domain.vo.req.EmojiAddReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.EmojiDelReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.EmojiResp;
import com.hitsz.badboyChat.common.user.mapper.UserEmojiMapper;
import com.hitsz.badboyChat.common.user.service.UserEmojiService;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【user_emoji(用户表情包)】的数据库操作Service实现
* @createDate 2024-02-21 13:27:06
*/
@Service
public class UserEmojiServiceImpl implements UserEmojiService {

    @Autowired
    private UserEmojiDao userEmojiDao;

    @Override
    public List<EmojiResp> getUserEmojiList(Long uid) {
        AssertUtil.isNotEmpty(uid,"请先登录哦");
        return userEmojiDao.getUserEmojiList(uid);
    }

    @Override
    @RedissionLock(key = "#uid")
    public void addEmoji(Long uid, EmojiAddReq req) {
        // 判断用户所拥有的表情包是否已经超出限制（30）
        Integer count = userEmojiDao.getUserEmojiCount(uid);
        AssertUtil.isFalse(count >= 30,"每个人只能添加30个表情包哦");
        // 判断表情包用户是否已经拥有过
       UserEmoji userEmoji = userEmojiDao.getUserEmojiById(uid,req.getEmojiUrl());
       AssertUtil.isEmpty(userEmoji, "你已经拥有过该表情包了哦");
       // 添加表情包
       UserEmoji insert = UserEmoji.builder().uid(uid).expressionUrl(req.getEmojiUrl()).build();
       userEmojiDao.save(insert);
    }

    @Override
    public void delEmoji(Long uid, EmojiDelReq req) {
        UserEmoji toDelEmoji = userEmojiDao.getById(req.getId());
        AssertUtil.isNotEmpty(toDelEmoji,"该表情包不存在哦");
        // 判断当前用户与要删除的表情包的拥有者是否一致
        AssertUtil.equal(uid,toDelEmoji.getUid(),"不能夺人所爱哦");
        userEmojiDao.removeById(req.getId());
    }
}




