package com.hitsz.badboyChat.common.chat.service.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.mapper.GroupMemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 13:34
 */
@Service
public class GroupMemBerDao extends ServiceImpl<GroupMemberMapper, GroupMember>{

    public GroupMember getMember(Long groupId, Long uid) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUid, uid)
                .eq(GroupMember::getIsDeleted, Boolean.FALSE)
                .one();
    }

    public List<Long> getGroupMembersUid(Long id) {
        List<GroupMember> groupMembers = lambdaQuery()
                .eq(GroupMember::getGroupId, id)
                .select(GroupMember::getUid)
                .list();
        return groupMembers.stream().map(GroupMember::getUid).collect(Collectors.toList());
    }

    public List<GroupMember> getMembers(Long groupId) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .list();
    }
}
