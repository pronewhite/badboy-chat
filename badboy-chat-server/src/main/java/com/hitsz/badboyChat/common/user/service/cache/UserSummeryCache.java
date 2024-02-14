package com.hitsz.badboyChat.common.user.service.cache;

import cn.hutool.system.UserInfo;
import com.hitsz.badboyChat.common.constant.RedisKey;
import com.hitsz.badboyChat.common.enums.ItemTypeEnum;
import com.hitsz.badboyChat.common.service.cache.AbstractRedisStringCache;
import com.hitsz.badboyChat.common.user.dao.UserBackpackDao;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.domain.entity.ItemConfig;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.entity.UserBackpack;
import com.hitsz.badboyChat.common.user.domain.vo.resp.SummeryInfoResp;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/12 15:22
 */
@Component
public class UserSummeryCache extends AbstractRedisStringCache<Long, SummeryInfoResp> {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_SUMMERY_KEY, uid);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60 * 60L;
    }

    @Override
    protected Map<Long, SummeryInfoResp> load(List<Long> uidList) {

        Map<Long, User> userMap = userInfoCache.getBatch(uidList);
        // 拿到列表用户的徽章信息
        List<ItemConfig> items = itemCache.getItemByType(ItemTypeEnum.BADGE.getType());
        List<UserBackpack> userBackpacks = userBackpackDao.getItemByIds(uidList, items);
        // 将userBackPacks按照用户id分组
        Map<Long, List<UserBackpack>> userBackpacksMap = userBackpacks.stream().collect(Collectors.groupingBy(UserBackpack::getUid));
        // 更新最后一次更新的时间
        return uidList.stream().map(uid -> {
            SummeryInfoResp summeryInfoResp = new SummeryInfoResp();
            User user = userMap.get(uid);
            if(user == null){
                return null;
            }
            List<UserBackpack> userBackPack = userBackpacksMap.getOrDefault(uid, new ArrayList<>());
            summeryInfoResp.setUid(uid);
            summeryInfoResp.setName(user.getName());
            summeryInfoResp.setAvatar(user.getAvatar());
            summeryInfoResp.setLocPlace(user.getIpInfo().getUpdateIpDetail().getCity());
            summeryInfoResp.setWearingItemId(user.getItemId());
            summeryInfoResp.setItemList(userBackPack.stream().map(UserBackpack::getItemId).collect(Collectors.toList()));
            return summeryInfoResp;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SummeryInfoResp::getUid, Function.identity()));
    }
}
