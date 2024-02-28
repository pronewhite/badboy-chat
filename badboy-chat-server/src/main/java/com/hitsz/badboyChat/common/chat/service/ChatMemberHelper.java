package com.hitsz.badboyChat.common.chat.service;

import com.hitsz.badboyChat.common.enums.UserActiveStatusEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/28 15:21
 */
public class ChatMemberHelper {

    private static final String SPLIT = "_";


    public static Pair<UserActiveStatusEnum, String> getCursor(String cursor) {
        UserActiveStatusEnum status = UserActiveStatusEnum.ONLINE;
        String timeCursor = null;
        if(StringUtils.isNotEmpty(cursor)){
            String[] split = cursor.split(SPLIT);
            status = UserActiveStatusEnum.of(Integer.parseInt(split[0]));
            timeCursor = split[1];
        }
        return Pair.of(status, timeCursor);
    }

    public static String generateCursor(String cursor, UserActiveStatusEnum activeStatus) {
        return activeStatus.getStatus() + SPLIT + cursor;
    }
}
