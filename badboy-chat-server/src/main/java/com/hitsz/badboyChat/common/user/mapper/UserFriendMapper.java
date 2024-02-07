package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.domain.vo.req.CursorPageBaseReq;
import com.hitsz.badboyChat.common.domain.vo.resp.CursorPageBaseResp;
import com.hitsz.badboyChat.common.user.domain.entity.UserFriend;
import com.hitsz.badboyChat.common.user.utils.CursorUtils;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【user_friend(用户联系人表)】的数据库操作Mapper
* @createDate 2024-02-07 13:38:07
* @Entity .domain.UserFriend
*/
@Mapper
public interface UserFriendMapper extends BaseMapper<UserFriend> {

}




