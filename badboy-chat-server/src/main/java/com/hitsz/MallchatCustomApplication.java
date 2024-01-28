package com.hitsz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author badboy
 * @date 2021/05/27
 */
@SpringBootApplication(scanBasePackages = {"com.hitsz"})
@MapperScan({"com/hitsz/badboyChat/common/user/mapper/**"})
public class MallchatCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallchatCustomApplication.class,args);
    }

}