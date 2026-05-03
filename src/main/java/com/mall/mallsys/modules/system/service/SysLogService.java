package com.mall.mallsys.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallsys.modules.system.entity.SysLog;

import java.time.LocalDateTime;

public interface SysLogService extends IService<SysLog> {

    /**
     * 分页查询操作日志（支持条件筛选）
     *
     * @param page      分页对象
     * @param username  操作人（模糊查询，可为 null）
     * @param operation 操作描述（模糊查询，可为 null）
     * @param status    操作状态：0-失败，1-成功，null-不限
     * @param startTime 开始时间（可为 null）
     * @param endTime   结束时间（可为 null）
     */
    Page<SysLog> getLogPage(Page<SysLog> page, String username, String operation,
                            Integer status, LocalDateTime startTime, LocalDateTime endTime);
}