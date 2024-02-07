package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【role(角色表)】的数据库操作Mapper
* @createDate 2024-02-04 12:37:18
* @Entity .domain.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}




