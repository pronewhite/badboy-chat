package com.hitsz.badboyChat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hitsz.badboyChat.common.enums.IdempotentEnum;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.vo.req.BlackUserReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.WearBadgeReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.BadgeResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author lenovo
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-01-20 20:34:09
*/
public interface UserService {

    /**
     *  用户注册
     * @param insert
     */
    void register(User insert);

    /**
     * 登录成功获取token
     * @param uid
     * @return
     */
    String login(Long uid);

    /**
     *  根据token获取uid
     * @param token 用户token
     * @return 用户id
     */
    Long getValidUid(String token);

    /**
     * 给key续期
     * @param token
     */
    void renewalKey(String token);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);

    List<BadgeResp> getBadges(Long uid);

    void wearBadge(Long uid, WearBadgeReq wearBadgeReq);

    void acquireItem(Long uid, Long itemId, IdempotentEnum itemPotent, String businessId);

    /**
     *  拉黑用户
     * @param blackUserReq 拉黑信息
     */
    void blackUser(BlackUserReq blackUserReq);
}
