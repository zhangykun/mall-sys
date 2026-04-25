package com.mall.mallsys.common.result;

import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "拒绝访问"),
    NOT_FOUND(404, "资源不存在"),

    // 用户相关
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "用户已被禁用"),
    TOKEN_INVALID(1004, "Token 无效或已过期");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}