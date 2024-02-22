package com.hitsz.badboyChat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hitsz.badboyChat.common.user.domain.entity.UserEmoji;
import com.hitsz.badboyChat.common.user.domain.vo.req.EmojiAddReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.EmojiDelReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.EmojiResp;

import java.util.List;

/**
* @author lenovo
* @description 针对表【user_emoji(用户表情包)】的数据库操作Service
* @createDate 2024-02-21 13:27:06
*/
public interface UserEmojiService {

    List<EmojiResp> getUserEmojiList(Long uid);

    void addEmoji(Long uid, EmojiAddReq req);

    void delEmoji(Long uid, EmojiDelReq req);
}
