package com.hitsz.badboyChat.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import com.hitsz.badboyChat.common.websocket.utils.NettyUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Optional;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/1/26 21:12
 */
public class HeadersCollectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
            // 获取token参数
            String token = Optional.ofNullable(urlBuilder.getQuery()).map(k -> k.get("token")).map(CharSequence::toString).orElse("");
            NettyUtils.setAttr(ctx.channel(), NettyUtils.TOKEN, token);
            // 需要将请求路径修改，否则如果路径不是webSocket匹配的路径，会导致连接失败
            request.setUri(urlBuilder.getPath().toString());
        }
        ctx.fireChannelRead(msg);
    }
}
