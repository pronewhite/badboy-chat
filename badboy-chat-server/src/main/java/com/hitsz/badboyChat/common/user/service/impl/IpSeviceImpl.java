package com.hitsz.badboyChat.common.user.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hitsz.badboyChat.common.domain.dto.IpResult;
import com.hitsz.badboyChat.common.user.domain.entity.IpDetail;
import com.hitsz.badboyChat.common.user.domain.entity.IpInfo;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import com.hitsz.badboyChat.common.user.service.IpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/3 18:09
 */
@Service
@Slf4j
public class IpSeviceImpl implements IpService {

    @Autowired
    private UserMapper userMapper;

    private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));
    @Override
    public void refreshIpDetailAsync(Long uid) {
        executor.execute(() -> {
            User user = userMapper.selectById(uid);
            IpInfo ipInfo = user.getIpInfo();
            String ip = ipInfo.needRefreshIp();
            if(ip == null){
                return;
            }
            // 根据ip获取ip详情
            IpDetail ipDetail = tryGetIpDetail(ip);
            if(Objects.nonNull(ipDetail)){
                ipInfo.refreshIpDetail(ipDetail);
                User updateUser = new User();
                updateUser.setId(uid);
                updateUser.setIpInfo(ipInfo);
                userMapper.updateById(updateUser);
            }
        });
    }

    private IpDetail tryGetIpDetail(String ip) {
        // 三次重试获取ip详情
        for(int i = 1; i <= 3; i++){
            IpDetail ipDetail = getIpDetail(ip);
            if(Objects.nonNull(ipDetail)){
                return ipDetail;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("getIpDetail error",e);
            }
        }
        return null;
    }

    private IpDetail getIpDetail(String ip) {
        String body = HttpUtil.get("https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc");
        try {
            IpResult<IpDetail> result = JSONUtil.toBean(body, new TypeReference<IpResult<IpDetail>>() {}, false);
            if (result.isSuccess()) {
                return result.getData();
            }
        } catch (Exception ignored) {
            log.error("getIpDetail error",ignored);
        }
        return null;
    }
}
