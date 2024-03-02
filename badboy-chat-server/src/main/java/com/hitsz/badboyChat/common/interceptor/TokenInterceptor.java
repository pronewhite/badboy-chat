package com.hitsz.badboyChat.common.interceptor;

import com.hitsz.badboyChat.common.enums.HttpResponseEnum;
import com.hitsz.badboyChat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 16:01
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String UID = "uid";
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        String token = getToken(authorization);
        Long uid = userService.getValidUid(token);
        if (Objects.nonNull(uid)) {
            request.setAttribute(UID, uid);
            return true;
        } else {
            // 判断要访问的接口是不是公共接口，如果没有登录并且不是公共接口那么返回401
            boolean isPublicUrl = isPublicUrl(request);
            if(!isPublicUrl){
                HttpResponseEnum.ACCESS_DENIED.sendError(response);
                return false;
            }
            return true;
        }
    }

    private boolean isPublicUrl(HttpServletRequest request) {
        String[] requestURI = request.getRequestURI().split("/");
        boolean isPublicUrl = requestURI.length > 3 && "public".equals(requestURI[3]);
        return isPublicUrl;
    }

    private String getToken(String authorization) {
        return Optional.ofNullable(authorization)
                .filter(auth -> auth.startsWith(TOKEN_PREFIX))
                .map(auth -> auth.substring(TOKEN_PREFIX.length()))
                .orElse(null);
    }
}
