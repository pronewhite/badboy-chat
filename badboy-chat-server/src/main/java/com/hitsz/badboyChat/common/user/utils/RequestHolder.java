package com.hitsz.badboyChat.common.user.utils;

import com.hitsz.badboyChat.common.domain.dto.RequestInfo;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/27 17:11
 * 请求上下文
 */
public class RequestHolder {

    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo){
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}
