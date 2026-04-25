package com.mall.mallsys;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 临时工具：生成 BCrypt 密码哈希
 * 运行 main 方法后复制输出结果到数据库
 */
public class BcryptGenTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String hash = encoder.encode("123456");
        System.out.println("BCrypt hash for '123456': " + hash);
    }
}
