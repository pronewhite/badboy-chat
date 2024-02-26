package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatRoomResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.MsgReadInfoResp;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.dao.ContactDao;
import com.hitsz.badboyChat.common.user.domain.entity.Contact;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.mapper.ContactMapper;
import com.hitsz.badboyChat.common.user.service.ContactService;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author lenovo
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2024-02-07 13:26:12
*/
@Service
public class ContactServiceImpl implements  ContactService{

    @Autowired
    private ContactDao contactDao;

    @Override
    public CursorPageBaseResp<ChatRoomResp> getChatRooms(Long uid, CursorPageBaseReq req) {
        // 用户登录与未登录看到的会话列表是不一致的
        if(Objects.nonNull(uid)){
            // 用户已登录


        }else{
            // 用户未登录

        }
        return null;
    }

    @Override
    public Collection<MsgReadInfoResp> getMsgReadInfo(Long uid, List<Message> msgs) {
        Map<Long, List<Message>> collect = msgs.stream().collect(Collectors.groupingBy(Message::getRoomId));
        AssertUtil.equal(collect.size(), 1, "只能查询一个房间中的消息已读未读数");
        Long roomId = collect.keySet().iterator().next();
        Integer totalCount = contactDao.getTotalCount(roomId);
        return msgs.stream().map(msg -> {
            MsgReadInfoResp resp = new MsgReadInfoResp();
            resp.setMsgId(msg.getId());
            Integer readCount = contactDao.getReadCount(msg);
            resp.setReadCount(readCount);
            Integer unReadCount = totalCount - readCount;
            resp.setUnReadCount(unReadCount);
            return resp;
        }).collect(Collectors.toList());

    }
}




