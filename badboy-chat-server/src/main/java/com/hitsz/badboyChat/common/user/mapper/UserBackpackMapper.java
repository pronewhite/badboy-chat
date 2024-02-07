package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.UserBackpack;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author lenovo
* @description 针对表【user_backpack(用户背包表)】的数据库操作Mapper
* @createDate 2024-01-28 14:22:47
* @Entity .domain.UserBackpack
*/
@Mapper
public interface UserBackpackMapper extends BaseMapper<UserBackpack> {

    default Integer getItemByUidAndItemId(Long uid, Long itemId){
        return selectCount(new LambdaQueryWrapper<UserBackpack>()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getIsDeleted,Boolean.FALSE));
    }

    default List<UserBackpack> getItemByUidAndItemIds(Long uid, List<Long> items){
        return selectList(new LambdaQueryWrapper<UserBackpack>()
                .eq(UserBackpack::getUid,uid)
                .in(UserBackpack::getItemId,items)
                .eq(UserBackpack::getIsDeleted,Boolean.FALSE));
    }

    default List<UserBackpack> getItemByUid(Long uid){
        return selectList(new LambdaQueryWrapper<UserBackpack>()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getIsDeleted,Boolean.FALSE));
    }

    default UserBackpack getItemByItenPotent(String itemPotent){
        return selectOne(new LambdaQueryWrapper<UserBackpack>()
                .eq(UserBackpack::getIdempotent,itemPotent)
                .eq(UserBackpack::getIsDeleted,Boolean.FALSE));
    }

}




