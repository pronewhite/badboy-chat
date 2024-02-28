package com.hitsz.badboyChat.common.chat.service.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.chat.enums.UserRoomRoleEnum;
import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.mapper.GroupMemberMapper;
import com.hitsz.badboyChat.common.user.service.cache.GroupMemberCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 13:34
 */
@Service
public class GroupMemBerDao extends ServiceImpl<GroupMemberMapper, GroupMember>{

    @Autowired
    private GroupMemberCache groupMemberCache;

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

    public List<GroupMember> getMembers(Long groupId, List<Long> uidList) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .in(GroupMember::getUid, uidList)
                .list();
    }

    public void removeMembers(Long groupId) {
         lambdaUpdate()
                .eq(GroupMember::getGroupId, groupId)
                .remove();
    }

    public void removeMember(Long id, Long uid) {
        lambdaUpdate()
                .eq(GroupMember::getGroupId, id)
                .eq(GroupMember::getUid, uid)
                .remove();
    }

    public List<GroupMember> getSelfGroupRoom(Long uid) {
        return lambdaQuery()
                .eq(GroupMember::getUid, uid)
                .eq(GroupMember::getRole, UserRoomRoleEnum.LEADER.getCode())
                .list();
    }

    public Set<Long> getAdminCount(Long groupId) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getRole, UserRoomRoleEnum.ADMIN.getCode())
                .list()
                .stream()
                .map(GroupMember::getUid)
                .collect(Collectors.toSet());
    }

    public void addAdmin(Long groupId, ArrayList<Long> longs) {
        LambdaUpdateWrapper<GroupMember> updateWrapper = new UpdateWrapper<GroupMember>().lambda()
                .eq(GroupMember::getGroupId, groupId)
                .in(GroupMember::getUid, longs)
                .set(GroupMember::getRole, UserRoomRoleEnum.ADMIN.getCode());
        this.update(updateWrapper);

    }

    public boolean isInGroupRoom(Long groupId, List<Long> uids) {
        List<Long> members = groupMemberCache.getMemberList(groupId).stream().map(GroupMember::getUid).collect(Collectors.toList());
        return members.containsAll(uids);
    }

    public void removeAdmin(Long groupId, List<Long> uids) {
        LambdaUpdateWrapper<GroupMember> updateWrapper = new UpdateWrapper<GroupMember>().lambda()
                .eq(GroupMember::getGroupId, groupId)
                .in(GroupMember::getUid, uids)
                .set(GroupMember::getRole, UserRoomRoleEnum.USER.getCode());
        this.update(updateWrapper);
    }
}
