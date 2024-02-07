package com.hitsz.badboyChat.common.user.service.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hitsz.badboyChat.common.user.domain.entity.Black;
import com.hitsz.badboyChat.common.user.domain.entity.ItemConfig;
import com.hitsz.badboyChat.common.user.domain.entity.UserRole;
import com.hitsz.badboyChat.common.user.mapper.BlackMapper;
import com.hitsz.badboyChat.common.user.mapper.ItemConfigMapper;
import com.hitsz.badboyChat.common.user.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/28 20:01
 */
@Component
public class UserCache {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private BlackMapper blackMapper;

    @Cacheable(value = "userCache", key = "'role'+#uid")
    public Set<Long> getUserRole(Long uid){
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUid, uid)
                .eq(UserRole::getIsDeleted, Boolean.FALSE));
        return userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
    }

    @Cacheable(value = "userCache", key = "'blackMap'")
    public Map<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> collect = blackMapper.selectList(new LambdaQueryWrapper<Black>()
                        .eq(Black::getIsDeleted, Boolean.FALSE))
                .stream()
                .collect(Collectors.groupingBy(Black::getType));
        Map<Integer, Set<String>> result = new HashMap<>();
        // 遍历collect
        for(Map.Entry<Integer, List<Black>> entry : collect.entrySet()){
            result.put(entry.getKey(), entry.getValue().stream().map(Black::getTarget).collect(Collectors.toSet()));
        }
        return result;
    }
}
