package com.hitsz.badboychat.transaction.dao;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboychat.transaction.domain.entity.SecureInvokeRecord;
import com.hitsz.badboychat.transaction.domain.enums.SecureInvokeRecordStatusEnum;
import com.hitsz.badboychat.transaction.mapper.SecureInvokeRecordMapper;
import com.hitsz.badboychat.transaction.service.SecureInvokeService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/14 20:50
 */
@Service
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord>{
    public List<SecureInvokeRecord> getWaitRetryRecords() {
        DateTime afterTime = DateUtil.offsetMinute(new Date(), (int)SecureInvokeService.RETRY_DELAY_TIME);
        return lambdaQuery()
                .eq(SecureInvokeRecord::getStatus, SecureInvokeRecordStatusEnum.WAIT_PROCESS.getCode())
                .lt(SecureInvokeRecord::getNextRetryTime, new Date())
                .lt(SecureInvokeRecord::getCreateTime, afterTime)
                .list();
    }
}
