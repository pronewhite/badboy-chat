package com.hitsz.badboyChat.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.hitsz.badboyChat.common.domain.dto.RequestInfo;
import com.hitsz.badboyChat.common.user.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 17:06
 */
@Component
public class InfoCollectInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = (Long)request.getAttribute(TokenInterceptor.UID);
        String ip = ServletUtil.getClientIP(request);
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(ip);
        requestInfo.setUid(uid);
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
