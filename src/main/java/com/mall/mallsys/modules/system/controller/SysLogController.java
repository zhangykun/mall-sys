package com.mall.mallsys.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.mallsys.common.annotation.Log;
import com.mall.mallsys.common.result.Result;
import com.mall.mallsys.modules.system.entity.SysLog;
import com.mall.mallsys.modules.system.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/system/log")
@Tag(name = "操作日志管理")
@PreAuthorize("hasAuthority('sys:log:list')")
public class SysLogController {

    private final SysLogService sysLogService;

    public SysLogController(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * 分页查询操作日志
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询操作日志")
    public Result<Page<SysLog>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        Page<SysLog> page = sysLogService.getLogPage(
                new Page<>(current, size), username, operation, status, startTime, endTime
        );
        return Result.success(page);
    }

    /**
     * 查询日志详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询日志详情")
    public Result<SysLog> getById(@PathVariable Long id) {
        return Result.success(sysLogService.getById(id));
    }

    /**
     * 批量删除日志
     */
    @Log("批量删除日志")
    @DeleteMapping
    @Operation(summary = "批量删除日志")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        sysLogService.removeByIds(ids);
        return Result.success();
    }

    /**
     * 清空所有日志（谨慎使用）
     */
    @Log("清空所有日志")
    @DeleteMapping("/clear")
    @Operation(summary = "清空所有日志")
    public Result<Void> clearAll() {
        sysLogService.remove(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>());
        return Result.success();
    }
}