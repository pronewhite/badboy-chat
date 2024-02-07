package com.hitsz.badboyChat.common.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitsz.badboyChat.common.user.domain.entity.Black;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lenovo
* @description 针对表【black(黑名单)】的数据库操作Mapper
* @createDate 2024-02-04 12:37:17
* @Entity .domain.Black
*/
@Mapper
public interface BlackMapper extends BaseMapper<Black> {

}




