package com.hitsz.badboyChat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.ItemConfig;
import com.hitsz.badboyChat.common.user.domain.entity.UserBackpack;
import com.hitsz.badboyChat.common.user.mapper.UserBackpackMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/12 15:44
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack>{
    public List<UserBackpack> getItemByIds(List<Long> uidList, List<ItemConfig> items) {
        return lambdaQuery().in(UserBackpack::getUid, uidList)
                .in(UserBackpack::getItemId, items)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getCode())
                .list();
    }
}
