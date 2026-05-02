package com.mall.mallsys.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallsys.modules.system.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    /**
     * 根据用户 ID 获取菜单树
     */
    List<SysMenu> getTreeListByUserId(Long userId);

    /**
     * 构建树形菜单
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus, Long parentId);
}