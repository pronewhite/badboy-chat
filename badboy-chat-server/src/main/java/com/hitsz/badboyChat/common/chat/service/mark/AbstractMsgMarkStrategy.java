package com.hitsz.badboyChat.common.chat.service.mark;

import com.hitsz.badboyChat.common.chat.domain.dto.ChatMessageMarkDTO;
import com.hitsz.badboyChat.common.chat.enums.MsgMarkActionTypeEnum;
import com.hitsz.badboyChat.common.chat.enums.MsgMarkTypeEnum;
import com.hitsz.badboyChat.common.chat.service.factory.MsgMarkFactory;
import com.hitsz.badboyChat.common.enums.YesOrNoEnum;
import com.hitsz.badboyChat.common.event.MsgMarkEvent;
import com.hitsz.badboyChat.common.user.dao.MessageMarkDao;
import com.hitsz.badboyChat.common.user.domain.entity.MessageMark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/25 15:42
 */
public abstract class AbstractMsgMarkStrategy {

    @Autowired
    private MessageMarkDao messageMarkDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public abstract MsgMarkTypeEnum getType();

    @PostConstruct
    public void init(){
        MsgMarkFactory.register(getType().getCode(), this);
    }

    @Transactional(rollbackFor = Exception.class)
    public void mark(Long uid, Long msgId){
        doMark(uid, msgId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void unMark(Long uid, Long msgId){
        doUnMark(uid, msgId);
    }

    protected void doUnMark(Long uid, Long msgId) {
        exec(uid, msgId, MsgMarkActionTypeEnum.CANCLE_MARK);
    }

    protected void doMark(Long uid, Long msgId){
        exec(uid, msgId, MsgMarkActionTypeEnum.MARK);
    }

    /**
     * 核心逻辑，收口了标记消息的主要流程
     * @param uid 标记消息的用户
     * @param msgId 标记的消息id
     * @param actionType 标记的类型
     */
    private void exec(Long uid, Long msgId, MsgMarkActionTypeEnum actionType){
        // 取出消息标记类型
        Integer markType = getType().getCode();
        // 操作类型
        Integer actType = actionType.getCode();
        MessageMark oldMsgMark = messageMarkDao.getMessageMark(uid, msgId);
        // 修改当前消息的标记类型以及状态
        MessageMark mark = MessageMark.builder()
                .id(Optional.ofNullable(oldMsgMark).map(MessageMark::getId).orElse(null))
                .msgId(msgId)
                .uid(uid)
                .type(markType)
                .status(transformType(actType))
                .build();
        boolean saveOrUpdate = messageMarkDao.saveOrUpdate(mark);
        if(saveOrUpdate){
            applicationEventPublisher.publishEvent(new MsgMarkEvent(this, new ChatMessageMarkDTO(uid, msgId, actType, markType)));
        }
    }

    private Integer transformType(Integer actType) {
        if(actType == MsgMarkActionTypeEnum.MARK.getCode()){
            return YesOrNoEnum.NO.getCode();
        }else if (actType == MsgMarkActionTypeEnum.CANCLE_MARK.getCode()){
            return YesOrNoEnum.YES.getCode();
        }
        return null;
    }

}
