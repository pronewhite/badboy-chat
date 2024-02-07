package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.mapper.GroupMemberMapper;
import com.hitsz.badboyChat.common.user.service.GroupMemberService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【group_member(群成员表)】的数据库操作Service实现
* @createDate 2024-02-07 13:26:12
*/
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService {

}




