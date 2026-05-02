package com.mall.mallsys.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.modules.system.entity.SysUser;
import com.mall.mallsys.modules.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/system/user")
@Tag(name = "系统用户管理")
public class SysUserController {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;

    public SysUserController(SysUserService sysUserService, PasswordEncoder passwordEncoder) {
        this.sysUserService = sysUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户")
    public Result<Page<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<SysUser> page = sysUserService.page(new Page<>(current, size),
                new LambdaQueryWrapper<SysUser>()
                        .like(StringUtils.hasText(keyword), SysUser::getUsername, keyword)
                        .orderByDesc(SysUser::getCreateTime)
        );
        return Result.success(page);
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    /**
     * 新增用户
     */
    @PostMapping
    @Operation(summary = "新增用户")
    public Result<Void> add(@Validated @RequestBody SysUser user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sysUserService.save(user);
        return Result.success();
    }

    /**
     * 修改用户
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改用户")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody SysUser user) {
        user.setId(id);
        sysUserService.updateById(user);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.success();
    }
}