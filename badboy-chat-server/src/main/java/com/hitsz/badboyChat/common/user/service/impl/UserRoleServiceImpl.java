package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.UserRole;
import com.hitsz.badboyChat.common.user.mapper.UserRoleMapper;
import com.hitsz.badboyChat.common.user.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user_role(用户角色关系表)】的数据库操作Service实现
* @createDate 2024-02-04 12:37:18
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

}




