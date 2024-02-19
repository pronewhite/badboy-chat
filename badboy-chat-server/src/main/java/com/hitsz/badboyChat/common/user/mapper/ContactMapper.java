package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.Contact;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author lenovo
* @description 针对表【contact(会话列表)】的数据库操作Mapper
* @createDate 2024-02-07 13:26:12
* @Entity .domain.Contact
*/
public interface ContactMapper extends BaseMapper<Contact> {

    void refreshOrCreateActiveTime(@Param("roomId") Long roomId, @Param("memberList") List<Long> memberList, @Param("msgId") Long msgId, @Param("activeTime") Date activeTime);
}




