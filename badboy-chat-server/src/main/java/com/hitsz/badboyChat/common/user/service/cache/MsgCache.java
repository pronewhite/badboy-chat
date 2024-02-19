package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/17 16:53
 */
@Component
public class MsgCache {

    @Autowired
    private MessageDao messageDao;

    @Cacheable(cacheNames = "msgCache", key = "'message' + #msgId")
    public Message getMsg(Long msgId) {
        return messageDao.getById(msgId);
    }

    @CacheEvict(cacheNames = "msgCache", key = "'message' + #msgId")
    public Message evitMsgCache(Long msgId) {
        return null;
    }
}
