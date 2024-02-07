package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【message(消息表)】的数据库操作Mapper
* @createDate 2024-02-07 13:26:12
* @Entity .domain.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




