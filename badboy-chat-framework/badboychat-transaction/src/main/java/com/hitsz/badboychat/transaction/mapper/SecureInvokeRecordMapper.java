package com.hitsz.badboychat.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboychat.transaction.domain.entity.SecureInvokeRecord;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【secure_invoke_record(本地消息表)】的数据库操作Mapper
* @createDate 2024-02-14 20:43:43
* @Entity .domain.SecureInvokeRecord
*/
@Mapper
public interface SecureInvokeRecordMapper extends BaseMapper<SecureInvokeRecord> {

}




