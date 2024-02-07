package com.hitsz.badboyChat.common.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.mapper.MessageMapper;
import com.hitsz.badboyChat.common.user.service.MessageService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-02-07 13:26:12
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

}




