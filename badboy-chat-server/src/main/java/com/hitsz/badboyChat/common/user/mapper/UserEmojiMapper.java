package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.UserEmoji;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【user_emoji(用户表情包)】的数据库操作Mapper
* @createDate 2024-02-21 13:27:06
* @Entity .domain.UserEmoji
*/
@Mapper
public interface UserEmojiMapper extends BaseMapper<UserEmoji> {

}




