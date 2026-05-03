package com.mall.mallsys.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.mallsys.common.annotation.Log;
import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.modules.system.entity.SysRole;
import com.mall.mallsys.modules.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/system/role")
@Tag(name = "角色管理")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询角色")
    public Result<Page<SysRole>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<SysRole> page = sysRoleService.page(new Page<>(current, size),
                new LambdaQueryWrapper<SysRole>()
                        .like(keyword != null, SysRole::getRoleName, keyword)
                        .orderByDesc(SysRole::getCreateTime)
        );
        return Result.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询角色详情")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }

    @Log("新增角色")
    @PostMapping
    @Operation(summary = "新增角色")
    public Result<Void> add(@Validated @RequestBody SysRole role) {
        sysRoleService.save(role);
        return Result.success();
    }

    @Log("修改角色")
    @PutMapping("/{id}")
    @Operation(summary = "修改角色")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody SysRole role) {
        role.setId(id);
        sysRoleService.updateById(role);
        return Result.success();
    }

    @Log("删除角色")
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public Result<Void> delete(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}/menuIds")
    @Operation(summary = "获取角色关联的菜单 ID")
    public Result<List<Long>> getMenuIds(@PathVariable Long id) {
        return Result.success(sysRoleService.getMenuIdsByRoleId(id));
    }

    @Log("分配菜单")
    @PutMapping("/{id}/menuIds")
    @Operation(summary = "为角色分配菜单")
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        sysRoleService.assignMenus(id, menuIds);
        return Result.success();
    }
}
