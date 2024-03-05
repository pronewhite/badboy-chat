package com.hitsz.badboyChat.common.chat.service.strategy;


import com.hitsz.badboyChat.common.enums.CommonErrorEnum;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;

import java.util.HashMap;
import java.util.Map;

public class MsgHandlerFactory {
    private static final Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    public static AbstractMsgHandler getStrategyNoNull(Integer code) {
        AbstractMsgHandler strategy = STRATEGY_MAP.get(code);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_VALID);
        return strategy;
    }
}
