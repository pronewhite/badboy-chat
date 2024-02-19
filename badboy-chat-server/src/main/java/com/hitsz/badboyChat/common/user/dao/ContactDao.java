package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.Contact;
import com.hitsz.badboyChat.common.user.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 22:54
 */
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact>{
    public void refreshOrCreateActiveTime(Long roomId, List<Long> memberList, Long msgId, Date createTime) {
        baseMapper.refreshOrCreateActiveTime(roomId, memberList, msgId, createTime);
    }

    public Long getLastMsgId(Long uid, Long roomId) {
        Contact contact = lambdaQuery()
                .eq(Contact::getUid, uid)
                .eq(Contact::getRoomId, roomId)
                .select(Contact::getLastMsgId)
                .one();
        return contact.getLastMsgId();
    }
}
