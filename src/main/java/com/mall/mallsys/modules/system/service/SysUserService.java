package com.mall.mallsys.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallsys.modules.system.dto.LoginRequest;
import com.mall.mallsys.modules.system.entity.SysMenu;
import com.mall.mallsys.modules.system.entity.SysUser;
import com.mall.mallsys.modules.system.vo.LoginResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * 系统用户服务接口
 */
public interface SysUserService extends IService<SysUser>, UserDetailsService {

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户登出
     */
    void logout(String token);

    /**
     * 获取当前用户信息
     */
    SysUser getCurrentUser();

    /**
     * 获取用户权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 获取用户菜单树
     */
    List<SysMenu> getUserMenus(Long userId);

    /**
     * 根据用户名加载用户信息（实现 UserDetailsService 接口）
     * Spring Security 认证时自动调用此方法
     */
    @Override
    UserDetails loadUserByUsername(String username);
}