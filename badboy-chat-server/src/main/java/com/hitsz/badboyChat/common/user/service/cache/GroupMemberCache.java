package com.hitsz.badboyChat.common.user.service.cache;

import com.hitsz.badboyChat.common.chat.service.dao.GroupMemBerDao;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/27 18:01
 */
@Component
public class GroupMemberCache {

    @Autowired
    private GroupMemBerDao groupMemBerDao;

    @Cacheable(value = "groupMember", key = "'group:'+ #groupId")
    public List<GroupMember> getMemberList(Long groupId){
        return groupMemBerDao.getMembers(groupId);
    }

    @CacheEvict(value = "groupMember", key = "'group:'+ #groupId")
    public List<GroupMember> evictMemberUidList(Long groupId) {
        return null;
    }
}
