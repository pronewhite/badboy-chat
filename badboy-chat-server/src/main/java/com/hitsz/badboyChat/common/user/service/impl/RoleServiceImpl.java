package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.enums.RoleTypeEnum;
import com.hitsz.badboyChat.common.user.domain.entity.Role;
import com.hitsz.badboyChat.common.user.mapper.RoleMapper;
import com.hitsz.badboyChat.common.user.service.RoleService;
import com.hitsz.badboyChat.common.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
* @author lenovo
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2024-02-04 12:37:18
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleTypeEnum roleTypeEnum) {
        Set<Long> userRole = userCache.getUserRole(uid);
        return userRole.contains(roleTypeEnum.getCode()) || isAdmin(userRole);
    }

    private boolean isAdmin(Set<Long> userRole) {
        return Objects.requireNonNull(userRole).contains(RoleTypeEnum.ADMIN.getCode());
    }
}




