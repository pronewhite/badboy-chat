package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.MessageMark;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【message_mark(消息标记表)】的数据库操作Mapper
* @createDate 2024-02-15 16:51:15
* @Entity .domain.MessageMark
*/
@Mapper
public interface MessageMarkMapper extends BaseMapper<MessageMark> {

}




