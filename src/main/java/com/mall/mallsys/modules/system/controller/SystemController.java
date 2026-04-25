package com.mall.mallsys.modules.system.controller;

import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.modules.system.entity.SysUser;
import com.mall.mallsys.modules.system.dto.LoginRequest;
import com.mall.mallsys.modules.system.vo.LoginResponse;
import com.mall.mallsys.modules.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 系统登录管理
 */
@Slf4j
@RestController
@RequestMapping("/api/system")
@Tag(name = "系统登录管理")
public class SystemController {

    private final SysUserService sysUserService;

    public SystemController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = sysUserService.login(request);
        return Result.success(response);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public Result<Void> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        sysUserService.logout(token);
        return Result.success();
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public Result<SysUser> getInfo() {
        SysUser user = sysUserService.getCurrentUser();
        return Result.success(user);
    }
}