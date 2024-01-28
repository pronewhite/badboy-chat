package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.ItemConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author lenovo
* @description 针对表【item_config(功能物品配置表)】的数据库操作Mapper
* @createDate 2024-01-28 14:22:47
* @Entity .domain.ItemConfig
*/
@Mapper
public interface ItemConfigMapper extends BaseMapper<ItemConfig> {

    default List<ItemConfig> getItemByType(Integer itemType){
        return selectList(new LambdaQueryWrapper<ItemConfig>()
                .eq(ItemConfig::getType,itemType)
                .eq(ItemConfig::getIsDeleted,Boolean.FALSE));
    }
}




