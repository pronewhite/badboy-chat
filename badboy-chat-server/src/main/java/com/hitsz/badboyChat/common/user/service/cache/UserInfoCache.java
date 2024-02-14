package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.constant.RedisKey;
import com.hitsz.badboyChat.common.service.cache.AbstractRedisStringCache;
import com.hitsz.badboyChat.common.user.dao.UserDao;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/12 15:30
 */
@Component
public class UserInfoCache extends AbstractRedisStringCache<Long, User> {

    @Autowired
    private UserDao userDao;

    @Override
    protected String getKey(Long req) {
        return RedisKey.getKey(RedisKey.USER_INFO_KEY, req);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60 * 60L;
    }

    @Override
    protected Map<Long, User> load(List<Long> req) {
        List<User> users = userDao.listByIds(req);
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }
}
