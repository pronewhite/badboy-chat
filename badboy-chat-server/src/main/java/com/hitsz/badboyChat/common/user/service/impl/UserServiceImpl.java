package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.annotation.RedissionLock;
import com.hitsz.badboyChat.common.constant.RedisKey;
import com.hitsz.badboyChat.common.enums.BlackTypeEnum;
import com.hitsz.badboyChat.common.enums.IdempotentEnum;
import com.hitsz.badboyChat.common.enums.ItemEnum;
import com.hitsz.badboyChat.common.enums.ItemTypeEnum;
import com.hitsz.badboyChat.common.event.UserBlackEvent;
import com.hitsz.badboyChat.common.event.UserRegisterEvent;
import com.hitsz.badboyChat.common.service.LockService;
import com.hitsz.badboyChat.common.user.domain.entity.Black;
import com.hitsz.badboyChat.common.user.domain.entity.ItemConfig;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.domain.entity.UserBackpack;
import com.hitsz.badboyChat.common.user.domain.vo.req.BlackUserReq;
import com.hitsz.badboyChat.common.user.domain.vo.req.WearBadgeReq;
import com.hitsz.badboyChat.common.user.domain.vo.resp.BadgeResp;
import com.hitsz.badboyChat.common.user.domain.vo.resp.UserInfoResp;
import com.hitsz.badboyChat.common.user.mapper.*;
import com.hitsz.badboyChat.common.user.service.UserBackpackService;
import com.hitsz.badboyChat.common.user.service.UserService;
import com.hitsz.badboyChat.common.user.service.adapter.UserAdapter;
import com.hitsz.badboyChat.common.user.service.cache.ItemCache;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import com.hitsz.badboyChat.common.user.utils.JwtUtils;
import com.hitsz.badboyChat.common.user.utils.RedisCommonProcessor;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lenovo
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-01-20 20:34:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    public static final long TIME_EXPIRE = 7L;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisCommonProcessor redisCommonProcessor;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserBackpackMapper userBackpackMapper;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    @Lazy
    private UserServiceImpl userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private BlackMapper blackMapper;


    @Override
    public void register(User insert) {
        userMapper.insert(insert);
        //推送用户注册的事件，用户注册成功后系统会给用户发送系列物品
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, insert));
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        redisCommonProcessor.set(getUserTokenKey(uid), token, TIME_EXPIRE, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        // 获取uid，就算token过期了，也能获取到uid，因为根据jwt的算法，唯一的uid对应唯一的token
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return null;
        }
        String key = getUserTokenKey(uid);
        String oldToken = (String) redisCommonProcessor.get(key);
        // 判断oldToken是否过期，如果过期，直接返回null
        if (oldToken == null) {
            return null;
        }
        // 判断token是否已经更换，
        boolean isTokenChanged = Objects.equals(oldToken, token);
        return isTokenChanged ? null : uid;
    }

    @Override
    @Async
    public void renewalKey(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        // 获取key的过期时间
        Long expireDays = redisCommonProcessor.getExpire(userTokenKey, TimeUnit.DAYS);
        if (expireDays == -2) {
            return;
        }
        if(expireDays <= 1){
            redisCommonProcessor.expire(userTokenKey, TIME_EXPIRE, TimeUnit.DAYS);
        }

    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        AssertUtil.isNotEmpty(uid, "uid不能为空");
        User user = userMapper.selectById(uid);
        // 获取用户改名卡数量
        Integer modifyNameCardCount = userBackpackMapper.getItemByUidAndItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfoResp(user,modifyNameCardCount);
    }

    @Override
    public void modifyName(Long uid, String name) {
        AssertUtil.isNotEmpty(uid,"uid不能为空");
        User user = userMapper.selectById(uid);
        AssertUtil.isNotEmpty(user,"用户不存在");
        // 通过名称获取用户
        User userByName = userMapper.getUserByName(name);
        AssertUtil.isEmpty(userByName,"用户名已存在,请更换用户名");
        user.setName(name);
        userMapper.updateById(user);
    }

    @Override
    public List<BadgeResp> getBadges(Long uid) {
        // 所有用户都需要显示所有徽章，所以所有的徽章列表可以从缓存中获取
        List<ItemConfig> items = itemCache.getItemByType(ItemTypeEnum.BADGE.getType());
        // 获取用户拥有的徽章
        List<UserBackpack> userBadges = userBackpackMapper.getItemByUidAndItemIds(uid,items.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        User user = userMapper.selectById(uid);
        return UserAdapter.buildBadgeResp(items,userBadges,user);
    }

    @Override
    public void wearBadge(Long uid, WearBadgeReq wearBadgeReq) {
        User user = userMapper.selectById(uid);
        // 如果当前佩戴的徽章是传入的徽章，则不需要重复佩戴
        AssertUtil.notEqual(user.getItemId(), wearBadgeReq.getItemId(),"您已经佩戴过该徽章了");
        List<UserBackpack> userBackpacks = userBackpackMapper.getItemByUid(uid);
        // 判断该item是否是徽章
        AssertUtil.equal(wearBadgeReq.getItemId(), ItemTypeEnum.BADGE.getType(),"该物品不是徽章，无法佩戴！");
        // 判断当前用户是否拥有该徽章
        boolean isHave = userBackpacks.stream().anyMatch(userBackpack -> userBackpack.getItemId().equals(wearBadgeReq.getItemId()));
        AssertUtil.isTrue(isHave,"您还未拥有该徽章，快去获取吧！");
        // 佩戴徽章
        user.setItemId(wearBadgeReq.getItemId());
        userMapper.updateById(user);
    }

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String itemPotent = getItemPotent(itemId,idempotentEnum.getType(), businessId);
        // 自己调用自己使事务和注解生效
        userService.doAcquireItem(uid, itemId, itemPotent);
    }

    @Override
    public void blackUser(BlackUserReq blackUserReq) {
        Long blackUserId = blackUserReq.getUserId();
        Black blackUser = new Black();
        blackUser.setType(BlackTypeEnum.UID.getCode());
        blackUser.setTarget(blackUserId.toString());
        blackMapper.insert(blackUser);
        User blackUserInfo = userMapper.selectById(blackUserId);
        // 封掉用户ip
        blackIp(blackUserInfo.getIpInfo().getCreateIp());
        blackIp(blackUserInfo.getIpInfo().getUpdateIp());
        applicationEventPublisher.publishEvent(new UserBlackEvent(this,blackUserInfo));
    }

    private void blackIp(String ip) {
        if(StringUtils.isBlank(ip)){
            return;
        }
        try{
            Black user = new Black();
            user.setType(BlackTypeEnum.IP.getCode());
            user.setTarget(ip);
            blackMapper.insert(user);
        }catch (Exception e){
            log.error("封号ip异常",e);
        }
    }

    @RedissionLock(key = "#itemPotent", waitTime = 5)
    public void doAcquireItem(Long uid, Long itemId, String itemPotent) {
        // 判断幂等
        UserBackpack userBackpack = userBackpackMapper.getItemByItenPotent(itemPotent);
        if(Objects.nonNull(userBackpack)){
            return;
        }
        // 发放物品
        UserBackpack insert = UserAdapter.buildSendItemUserBack(uid, itemId, itemPotent);
        userBackpackMapper.insert(insert);
    }

    private String getItemPotent(Long itemId, Integer type, String businessId) {
        return String.format("%d_%d_%s", itemId, type, businessId);
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_REDIS_KEY, uid);
    }

}




