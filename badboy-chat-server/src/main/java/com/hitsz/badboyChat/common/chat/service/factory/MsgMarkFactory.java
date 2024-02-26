package com.hitsz.badboyChat.common.chat.service.factory;

import com.hitsz.badboyChat.common.chat.service.mark.AbstractMsgMarkStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/25 15:47
 */
public class MsgMarkFactory {

    private final static Map<Integer, AbstractMsgMarkStrategy> STRATEGY_MAP = new HashMap<>();
    public static void register(Integer type, AbstractMsgMarkStrategy strategy){
        STRATEGY_MAP.put(type, strategy);
    }

    public static AbstractMsgMarkStrategy getStrategyOrNull(Integer type){
        return STRATEGY_MAP.get(type);
    }

}
