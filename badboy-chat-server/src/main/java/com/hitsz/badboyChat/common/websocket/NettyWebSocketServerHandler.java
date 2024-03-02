package com.hitsz.badboyChat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.hitsz.badboyChat.common.websocket.domain.enums.WSReqTypeEnum;
import com.hitsz.badboyChat.common.websocket.domain.vo.req.WSBaseReq;
import com.hitsz.badboyChat.common.websocket.service.WebSocketService;
import com.hitsz.badboyChat.common.websocket.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/20 15:51
 */
@ChannelHandler.Sharable
@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffline(ctx.channel());
    }

    /**
     * 用户下线
     * @param channel
     */
    private void userOffline(Channel channel) {
        // 移除用户与通道的绑定关系
        webSocketService.offline(channel);
        channel.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            // 握手认证
            webSocketService.authroize(ctx.channel(), NettyUtils.getAttr(ctx.channel(), NettyUtils.TOKEN));
        }else if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("长时间未读取到客户端的消息，断开连接");
                userOffline(ctx.channel());
            }
        }
    } @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case LOGIN:
                webSocketService.handleLoginReq(channelHandlerContext.channel());
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }


}
