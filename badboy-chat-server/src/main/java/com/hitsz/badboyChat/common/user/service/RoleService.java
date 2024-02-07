package com.hitsz.badboyChat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hitsz.badboyChat.common.enums.RoleTypeEnum;
import com.hitsz.badboyChat.common.user.domain.entity.Role;

/**
* @author lenovo
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2024-02-04 12:37:18
*/
public interface RoleService extends IService<Role> {

    boolean hasPower(Long uid, RoleTypeEnum roleTypeEnum);

}
