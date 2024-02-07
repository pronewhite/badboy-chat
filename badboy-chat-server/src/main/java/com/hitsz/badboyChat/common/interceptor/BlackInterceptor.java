package com.hitsz.badboyChat.common.interceptor;

import com.hitsz.badboyChat.common.domain.dto.RequestInfo;
import com.hitsz.badboyChat.common.enums.BlackTypeEnum;
import com.hitsz.badboyChat.common.enums.HttpResponseEnum;
import com.hitsz.badboyChat.common.user.service.UserService;
import com.hitsz.badboyChat.common.user.service.cache.UserCache;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 16:01
 */
@Component
public class BlackInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestInfo requestInfo = RequestHolder.get();
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        if(inBlackList(blackMap.get(BlackTypeEnum.UID.getCode()), requestInfo.getUid())){
            HttpResponseEnum.ACCESS_DENIED.sendError(response);
            return false;
        }
        if(inBlackList(blackMap.get(BlackTypeEnum.IP.getCode()), requestInfo.getIp())){
            HttpResponseEnum.ACCESS_DENIED.sendError(response);
            return false;
        }
        return true;
    }

    private boolean inBlackList(Set<String> blacks, Object target) {
        if(Objects.isNull(blacks)|| blacks.isEmpty() || Objects.isNull(target)){
            return false;
        }
        return blacks.contains(target.toString());
    }

}
