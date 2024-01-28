package com.hitsz.badboyChat.common.user.service.adapter;

import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.user.domain.entity.ItemConfig;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.entity.UserBackpack;
import com.hitsz.badboyChat.common.user.domain.vo.resp.BadgeResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/21 16:54
 */
public class UserAdapter {
    public static User buildInsert(String openId) {
        return  User.builder().openId(openId).build();
    }

    public static User buildFillUserInfo( User user,WxOAuth2UserInfo userInfo) {
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setName(userInfo.getNickname());
        return user;
    }

    public static UserInfoResp buildUserInfoResp(User user, Integer modifyNameCardCount) {
        return UserInfoResp.builder()
                .name(user.getName())
                .id(user.getId())
                .avatar(user.getAvatar())
                .modifyNameChance(modifyNameCardCount)
                .build();
    }

    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> items, List<UserBackpack> userBadges, User user) {
        if(Objects.isNull(user)){
            return Collections.emptyList();
        }
        Set<Long> userItemIds = userBadges.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return items.stream().map(itemConfig -> {
            BadgeResp badgeResp = new BadgeResp();
            BeanUtils.copyProperties(itemConfig, badgeResp);
            badgeResp.setObtain(userItemIds.contains(itemConfig.getId()) ? YesOrNoEnum.YES.getCode() :YesOrNoEnum.NO.getCode());
            badgeResp.setWearing(user.getItemId().equals(itemConfig.getId()) ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
            return badgeResp;
        }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder())
                .thenComparing(BadgeResp::getObtain, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
