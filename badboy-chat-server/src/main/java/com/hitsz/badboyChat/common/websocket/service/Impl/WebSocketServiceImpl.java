package com.hitsz.badboyChat.common.websocket.service.Impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hitsz.badboyChat.common.config.ThreadPoolConfig;
import com.hitsz.badboyChat.common.enums.RoleTypeEnum;
import com.hitsz.badboyChat.common.event.UserOnlineEvent;
import com.hitsz.badboyChat.common.user.domain.entity.User;
import com.hitsz.badboyChat.common.user.mapper.UserMapper;
import com.hitsz.badboyChat.common.user.service.RoleService;
import com.hitsz.badboyChat.common.user.service.UserService;
import com.hitsz.badboyChat.common.websocket.domain.dto.WSChannelUserDTO;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBaseResp;
import com.hitsz.badboyChat.common.websocket.domain.vo.resp.WSBlack;
import com.hitsz.badboyChat.common.websocket.service.WebSocketService;
import com.hitsz.badboyChat.common.websocket.service.adapter.WebSocketAdapter;
import com.hitsz.badboyChat.common.websocket.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/21 11:33
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    /**
     * 保存用户与ws通道的映射关系（包括登录用户与未登录用户）
     */
    private static final ConcurrentHashMap<Channel, WSChannelUserDTO> ON_LINE_WS_MAP = new ConcurrentHashMap<>();

    public static final int MAXIMUM_SIZE = 1000;
    public static final int HOURS = 1;
    public static final Duration DURATION = Duration.ofHours(HOURS);
    /**
     * 保存随机code和channel之间的关系
     */
    private static final Cache<Integer, Channel> CODE_CHANNEL_MAP = Caffeine.newBuilder().maximumSize(MAXIMUM_SIZE).expireAfterWrite(DURATION).build();

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private RoleService roleService;
    @Autowired
    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void connect(Channel channel) {
        ON_LINE_WS_MAP.put(channel, new WSChannelUserDTO());
    }

    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
        // 生成随机code
        Integer code = generateCode(channel);
        // 生成带参数的二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        // 将二维码推送给前端
        sendMsg(channel, WebSocketAdapter.buildLoginResp(wxMpQrCodeTicket));
    }

    @Override
    public void offline(Channel channel) {
        ON_LINE_WS_MAP.remove(channel);
        // todo 推送用户下线操作
    }

    @Override
    public void scanLoginSucess(Integer code, Long uid) {
        Channel channel = CODE_CHANNEL_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        // 移除code和channel的映射关系
        CODE_CHANNEL_MAP.invalidate(code);
        // 获取用户信息
        User user = userMapper.selectById(uid);
        // 获取token
        String token = userService.login(uid);
        loginSucess(channel, user, token);
    }

    @Override
    public void waitAuthroize(Integer code) {
        Channel channel = CODE_CHANNEL_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWaitAuthResp());
    }

    @Override
    public void authroize(Channel channel, String token) {
        Long uid = userService.getValidUid(token);
        if(Objects.nonNull(uid)){
            User user = userMapper.selectById(uid);
            loginSucess(channel, user,token);
        }else{
            // 向前端推送token失效消息
            sendMsg(channel, WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    @Override
    public void sendMsgToAll(WSBaseResp<WSBlack> resp, Long id) {
        ON_LINE_WS_MAP.forEach((channel,etx) -> {
            if(Objects.nonNull(etx.getUid()) && etx.getUid().equals(id)){
                return;
            }
            threadPoolTaskExecutor.execute(() -> {
                sendMsg(channel, resp);
            });
        });
    }

    /**
     *  登录成功后，完成相关操作
     * @param channel
     * @param user
     * @param token
     */
    private void loginSucess(Channel channel, User user,String token) {
        WSChannelUserDTO wsChannelUserDTO = ON_LINE_WS_MAP.get(channel);
        wsChannelUserDTO.setUid(String.valueOf(user.getId()));
        // 推送登录成功消息
        boolean hasPower = roleService.hasPower(user.getId(), RoleTypeEnum.ADMIN);
        sendMsg(channel, WebSocketAdapter.buildLoginSuccessResp(user,token,hasPower));
        // 推送用户在线事件
        user.setLastOptTime(new Date());
        user.refreshIp(NettyUtils.getAttr(channel, NettyUtils.IP));
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
    }

    /**
     *  通过通道向前端发送消息
     * @param channel 通道
     * @param wsResp 发送的消息
     */
    private void sendMsg(Channel channel, WSBaseResp<?> wsResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsResp)));
    }

    private Integer generateCode(Channel channel) {
        Integer code;
        // 循环生成code，直到生成一个不重复的CODE_CHANNEL_MAP中的key
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(CODE_CHANNEL_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }
}
