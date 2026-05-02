package com.mall.mallsys.modules.system.controller;

import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.modules.system.service.SysMenuService;
import com.mall.mallsys.modules.system.entity.SysMenu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 */
@Slf4j
@RestController
@RequestMapping("/api/system/menu")
@Tag(name = "菜单管理")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    /**
     * 获取用户菜单树
     */
    @GetMapping("/list")
    @Operation(summary = "获取用户菜单树")
    public Result<List<SysMenu>> getMenuList(Long userId) {
        List<SysMenu> menuList = sysMenuService.getTreeListByUserId(userId);
        return Result.success(menuList);
    }

    /**
     * 获取全量菜单树（管理用）
     */
    @GetMapping("/allTree")
    @Operation(summary = "获取全量菜单树")
    public Result<List<SysMenu>> getAllMenuTree() {
        List<SysMenu> allMenus = sysMenuService.list();
        return Result.success(sysMenuService.buildMenuTree(allMenus, 0L));
    }

    /**
     * 新增菜单
     */
    @PostMapping
    @Operation(summary = "新增菜单")
    public Result<Void> add(@Validated @RequestBody SysMenu menu) {
        sysMenuService.save(menu);
        return Result.success();
    }

    /**
     * 修改菜单
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改菜单")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody SysMenu menu) {
        menu.setId(id);
        sysMenuService.updateById(menu);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    public Result<Void> delete(@PathVariable Long id) {
        sysMenuService.removeById(id);
        return Result.success();
    }
}
