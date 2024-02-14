package com.hitsz.badboyChat.common.chat.service.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 11:24
 */
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message>{

}
