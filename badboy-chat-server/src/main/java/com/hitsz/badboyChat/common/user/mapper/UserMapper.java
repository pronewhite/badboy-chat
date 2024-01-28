package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-01-20 20:34:09
* @Entity .domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    default User getUserByOpenId(String openId){
        // 根据openId查询用户信息
        return selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenId, openId));
    }

    default User getUserByName(String name){
        return selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getName, name)
                .eq(User::getIsDeleted, Boolean.FALSE));
    }
}




