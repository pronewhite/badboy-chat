package com.hitsz.badboyChat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatRoomResp;
import com.hitsz.badboyChat.common.chat.domain.vo.resp.MsgReadInfoResp;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.Contact;
import com.hitsz.badboyChat.common.user.domain.entity.Message;

import java.util.Collection;
import java.util.List;

/**
* @author lenovo
* @description 针对表【contact(会话列表)】的数据库操作Service
* @createDate 2024-02-07 13:26:12
*/
public interface ContactService {

    /**
     * 获取会话列表
     * @param uid 用户id，每个人看到的会话肯定是不一样的
     * @param req 游标翻页请求
     * @return
     */
    CursorPageBaseResp<ChatRoomResp> getChatRooms(Long uid, CursorPageBaseReq req);

    Collection<MsgReadInfoResp> getMsgReadInfo(Long uid, List<Message> msgs);
}
