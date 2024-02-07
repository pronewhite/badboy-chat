package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.Contact;
import com.hitsz.badboyChat.common.user.mapper.ContactMapper;
import com.hitsz.badboyChat.common.user.service.ContactService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2024-02-07 13:26:12
*/
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
    implements ContactService {

}




