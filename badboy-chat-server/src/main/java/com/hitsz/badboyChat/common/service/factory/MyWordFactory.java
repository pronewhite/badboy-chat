package com.hitsz.badboyChat.common.service.factory;

import com.hitsz.badboyChat.common.user.dao.SensitiveDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/22 21:08
 */
@Component
public class MyWordFactory implements SensitiveWordFactory{

    @Autowired
    private SensitiveDao sensitiveDao;

    @Override
    public List<String> getSensitiveWords() {
        return sensitiveDao.getSensitiveWords();
    }
}
