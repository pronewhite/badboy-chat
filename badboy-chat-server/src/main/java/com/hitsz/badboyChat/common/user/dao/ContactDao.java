package com.hitsz.badboyChat.common.user.dao;

import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMsgReadInfoReq;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.Contact;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.mapper.ContactMapper;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/15 22:54
 */
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact>{
    @Autowired
    private  MessageDao messageDao;


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

    public Contact getByRoomIdAndUid(Long roomId, Long uid) {
        return lambdaQuery()
                .eq(Contact::getRoomId, roomId)
                .eq(Contact::getUid, uid)
                .one();
    }

    public Integer getTotalCount(Long roomId) {
        return lambdaQuery()
                .eq(Contact::getRoomId, roomId)
                .count();
    }

    public Integer getReadCount(Message msg) {
        return  lambdaQuery()
                .eq(Contact::getRoomId, msg.getRoomId())
                .ne(Contact::getUid, msg.getFromUid())
                .ge(Contact::getReadTime, msg.getCreateTime())
                .count();
    }

    public CursorPageBaseResp<Contact> getUnReadPage(Message message, CursorPageBaseReq req) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper ->{
            wrapper.eq(Contact::getRoomId, message.getRoomId());
            wrapper.ne(Contact::getUid, message.getFromUid());
            wrapper.lt(Contact::getReadTime, message.getCreateTime());
        },Contact::getReadTime);
    }

    public CursorPageBaseResp<Contact> getReadPage(Message message, ChatMsgReadInfoReq req) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper ->{
            wrapper.eq(Contact::getRoomId, message.getRoomId());
            wrapper.ne(Contact::getUid, message.getFromUid());
            wrapper.ge(Contact::getReadTime, message.getCreateTime());
        },Contact::getReadTime);
    }

    public CursorPageBaseResp<Contact> getContacts(Long uid, CursorPageBaseReq req) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper -> {
            wrapper.eq(Contact::getUid, uid);
        },Contact::getActiveTime);
    }

    public Map<Long, Integer> getUnReadCount(Long uid, List<Long> roomList) {
        if(Objects.isNull(roomList)){
            return new HashMap<>();
        }
        List<Contact> contacts = getContacts(uid, roomList);
        return contacts.stream().map(contact -> Pair.of(contact.getRoomId(), messageDao.getUnReadCount(contact.getRoomId(), contact.getReadTime())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public List<Contact> getContacts(Long uid, List<Long> roomList) {
        return lambdaQuery()
                .eq(Contact::getUid, uid)
                .in(Contact::getRoomId, roomList)
                .list();
    }

    public void removeContact(Long roomId) {
        lambdaUpdate()
                .eq(Contact::getRoomId, roomId)
                .remove();
    }

    public void removeContact(Long roomId, Long uid) {
        lambdaUpdate()
                .eq(Contact::getRoomId, roomId)
                .eq(Contact::getUid, uid)
                .remove();
    }
}
