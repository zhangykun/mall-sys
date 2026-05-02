package com.mall.mallsys.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallsys.modules.system.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户 ID 获取角色列表
     */
    List<SysRole> getRolesByUserId(Long userId);

    /**
     * 获取角色关联的菜单 ID 列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 为角色分配菜单
     */
    void assignMenus(Long roleId, List<Long> menuIds);
}