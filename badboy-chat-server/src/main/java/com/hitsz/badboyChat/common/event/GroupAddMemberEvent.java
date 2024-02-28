package com.hitsz.badboyChat.common.event;

import com.hitsz.badboyChat.common.user.domain.entity.GroupMember;
import com.hitsz.badboyChat.common.user.domain.entity.RoomGroup;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/28 13:51
 */
@Getter
public class GroupAddMemberEvent extends ApplicationEvent {

    private RoomGroup roomGroup;

    private List<GroupMember> members;

    private Long uid;

    public GroupAddMemberEvent(Object source, RoomGroup roomGroup, List<GroupMember> members, Long uid) {
        super(source);
        this.roomGroup = roomGroup;
        this.members = members;
        this.uid = uid;
    }
}
