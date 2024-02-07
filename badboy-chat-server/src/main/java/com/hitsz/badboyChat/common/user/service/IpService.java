package com.hitsz.badboyChat.common.user.service;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/3 18:04
 */
public interface IpService {

    /**
     * 异步更新用户ip
     * @param uid
     */
    void refreshIpDetailAsync(Long uid);
}
