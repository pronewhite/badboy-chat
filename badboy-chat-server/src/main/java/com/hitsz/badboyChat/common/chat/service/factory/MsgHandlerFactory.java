package com.hitsz.badboyChat.common.chat.service.factory;

import com.hitsz.badboyChat.common.chat.service.strategy.AbstractMsgHandler;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 10:59
 */
public class MsgHandlerFactory {

    private static Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(int code, AbstractMsgHandler msgHandler) {
        STRATEGY_MAP.put(code, msgHandler);
    }

    public static AbstractMsgHandler getStrategyOrNull(Integer msgType) {
        AbstractMsgHandler abstractMsgHandler = STRATEGY_MAP.get(msgType);
        return abstractMsgHandler;
    }
}
