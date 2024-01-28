package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.user.domain.entity.ItemConfig;
import com.hitsz.badboyChat.common.user.mapper.ItemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/28 20:01
 */
@Component
public class ItemCache {

    @Autowired
    private ItemConfigMapper itemConfigMapper;

    @Cacheable(value = "itemCache", key = "'itemsByType:'+#itemType")
    public List<ItemConfig> getItemByType(Integer itemType){
        return itemConfigMapper.getItemByType(itemType);
    }
}
