package com.hitsz.badboyChat.common.chat.service.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.hitsz.badboyChat.common.chat.domain.vo.req.ChatMessageReq;
import com.hitsz.badboyChat.common.chat.enums.MessageTypeEnum;
import com.hitsz.badboyChat.common.chat.service.adapter.MessageAdapter;
import com.hitsz.badboyChat.common.chat.service.dao.MessageDao;
import com.hitsz.badboyChat.common.chat.service.factory.MsgHandlerFactory;
import com.hitsz.badboyChat.common.user.domain.entity.Message;
import com.hitsz.badboyChat.common.user.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 10:56
 */
public abstract class AbstractMsgHandler<Req> {

    private Class<Req> bodyClass;

    @Autowired
    private MessageDao messageDao;

    @PostConstruct
    public void init(){
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMsgTypeEnum().getType(),this);
    }

    protected abstract MessageTypeEnum getMsgTypeEnum();

    public Long checkAndSaveMsg(ChatMessageReq req, Long uid){
        Req body = this.toBean(req.getMsgContent());
        // springMVC只能对ChatMessageReq中的第一层参数进行注解校验，
        // 对于更深层次的参数（消息内容）无法进行注解校验，因此需要在此处统一进行校验
        AssertUtil.allCheckValidateThrow(body);
        // 子类扩展校验
        checkMsg(body, req.getRoomId(), uid);
        Message insert = MessageAdapter.buildMessageInsert(req, uid);
        messageDao.save(insert);
        // 子类扩展保存
        saveMsg(insert, body);
        return insert.getId();
    }

    /**
     * 每一个子类都需要有自己的保存逻辑，所以将该方法设置为抽象方法
     * @param insert
     * @param body
     */
    protected abstract void saveMsg(Message insert, Req body);

    /**
     * 不是所有方法都需要有自己的校验逻辑，所以将该方法设置为非抽象方法
     * @param body
     * @param roomId
     * @param uid
     */
    protected void checkMsg(Req body, Long roomId, Long uid){

    }

    private Req toBean(Object msgContent) {
        if(bodyClass.isAssignableFrom(msgContent.getClass())){
            return (Req) msgContent;
        }
        return BeanUtil.toBean(msgContent,bodyClass);
    }

    public abstract Object showMsg(Message message) ;

    protected abstract Object showRespMsg(Message message);

    protected abstract Object showReplyMsg(Message message);

    public abstract Object showContactMsg(Message message);
}
