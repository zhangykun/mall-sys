package com.mall.mallsys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/*
* 商城管理后台启动类
* */
@SpringBootApplication
//@MapperScan("com.mall.mallsys.modules.*.mapper")

@EnableAsync
public class MallSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallSysApplication.class, args);
    }

}
