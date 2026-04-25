package com.mall.mallsys.modules.system.vo;

import com.mall.mallsys.modules.system.entity.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * 登录响应 VO
 */
@Data
public class LoginResponse {

    /**
     * Token
     */
    private String token;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 菜单列表
     */
    private List<SysMenu> menus;
}