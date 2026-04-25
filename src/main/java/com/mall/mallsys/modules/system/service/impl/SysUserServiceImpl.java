package com.mall.mallsys.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mall.mallsys.modules.system.entity.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallsys.common.exception.BusinessException;
import com.mall.mallsys.common.result.ResultCode;
import com.mall.mallsys.common.utils.JwtUtils;
import com.mall.mallsys.common.utils.RedisUtils;
import com.mall.mallsys.modules.system.dto.LoginRequest;
import com.mall.mallsys.modules.system.entity.SysRole;
import com.mall.mallsys.modules.system.entity.SysUser;
import com.mall.mallsys.modules.system.mapper.SysUserMapper;
import com.mall.mallsys.modules.system.service.SysMenuService;
import com.mall.mallsys.modules.system.service.SysRoleService;
import com.mall.mallsys.modules.system.service.SysUserService;
import com.mall.mallsys.modules.system.vo.LoginResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统用户服务实现
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    private static final String TOKEN_PREFIX = "token:";
    private static final long TOKEN_EXPIRE_DAYS = 1;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 查询用户
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername()));

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        // 3. 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 4. 生成 Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        // 5. 将 Token 存入 Redis
        String redisKey = TOKEN_PREFIX + user.getId();
        redisUtils.set(redisKey, token, TOKEN_EXPIRE_DAYS * 24 * 60 * 60);

        // 6. 获取用户权限和菜单
        List<String> permissions = getUserPermissions(user.getId());
        List<SysMenu> menus = sysMenuService.getTreeListByUserId(user.getId());

        // 7. 构建返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setPermissions(permissions);
        response.setMenus(menus);

        return response;
    }

    @Override
    public void logout(String token) {
        try {
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                Long userId = jwtUtils.getUserIdFromToken(token);
                String redisKey = TOKEN_PREFIX + userId;
                redisUtils.del(redisKey);
            }
        } catch (Exception e) {
            log.error("登出失败：{}", e.getMessage());
        }
    }

    @Override
    public SysUser getCurrentUser() {
        Object principal = org.springframework.security.core.context
                .SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SysUser) {
            return (SysUser) principal;
        }
        return null;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        // 超级管理员拥有所有权限
        List<SysRole> roles = sysRoleService.getRolesByUserId(userId);
        boolean isSuperAdmin = roles.stream()
                .anyMatch(r -> "super_admin".equals(r.getRoleKey()));
        if (isSuperAdmin) {
            List<String> allPermissions = new ArrayList<>();
            allPermissions.add("*:*:*");
            return allPermissions;
        }
        return baseMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<SysMenu> getUserMenus(Long userId) {
        return sysMenuService.getTreeListByUserId(userId);
    }

    /**
     * 实现 UserDetailsService 接口
     * Spring Security 认证时调用此方法加载用户信息和权限
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }

        if (user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已被禁用：" + username);
        }

        // 2. 查询用户权限列表（关键！没有权限 @PreAuthorize 会失效）
        List<String> permissions = getUserPermissions(user.getId());
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 3. 返回包含权限的 UserDetails
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}