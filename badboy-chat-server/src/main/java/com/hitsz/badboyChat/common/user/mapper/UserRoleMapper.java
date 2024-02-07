package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【user_role(用户角色关系表)】的数据库操作Mapper
* @createDate 2024-02-04 12:37:18
* @Entity .domain.UserRole
*/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}




