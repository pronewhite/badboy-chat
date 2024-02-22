package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【sensitive_word】的数据库操作Mapper
* @createDate 2024-02-21 17:14:31
* @Entity .domain.SensitiveWord
*/
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

}




