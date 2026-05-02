package com.mall.mallsys.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallsys.modules.system.entity.SysMenu;
import com.mall.mallsys.modules.system.mapper.SysMenuMapper;
import com.mall.mallsys.modules.system.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> getTreeListByUserId(Long userId) {
        // 1. 获取用户所有菜单（平铺）
        List<SysMenu> allMenus = baseMapper.selectMenusByUserId(userId);

        // 2. 构建树形结构
        return buildMenuTree(allMenus, 0L);
    }

    /**
     * 构建菜单树
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(parentId))
                .map(menu -> {
                    SysMenu node = new SysMenu();
                    node.setId(menu.getId());
                    node.setParentId(menu.getParentId());
                    node.setName(menu.getName());
                    node.setPath(menu.getPath());
                    node.setComponent(menu.getComponent());
                    node.setPermission(menu.getPermission());
                    node.setType(menu.getType());
                    node.setIcon(menu.getIcon());
                    node.setSort(menu.getSort());
                    // 递归加载子菜单
                    node.setChildren(buildMenuTree(menus, menu.getId()));
                    return node;
                })
                .collect(Collectors.toList());
    }
}