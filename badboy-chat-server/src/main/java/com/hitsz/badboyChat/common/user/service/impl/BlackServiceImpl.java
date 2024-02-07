package com.hitsz.badboyChat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitsz.badboyChat.common.user.domain.entity.Black;
import com.hitsz.badboyChat.common.user.mapper.BlackMapper;
import com.hitsz.badboyChat.common.user.service.BlackService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【black(黑名单)】的数据库操作Service实现
* @createDate 2024-02-04 12:37:17
*/
@Service
public class BlackServiceImpl extends ServiceImpl<BlackMapper, Black>
    implements BlackService {

}




