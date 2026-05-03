package com.mall.mallsys.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallsys.modules.system.entity.SysLog;
import com.mall.mallsys.modules.system.mapper.SysLogMapper;
import com.mall.mallsys.modules.system.service.SysLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public Page<SysLog> getLogPage(Page<SysLog> page, String username, String operation,
                                   Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();

        // 按操作人模糊查询
        wrapper.like(StringUtils.hasText(username), SysLog::getUsername, username);

        // 按操作描述模糊查询
        wrapper.like(StringUtils.hasText(operation), SysLog::getOperation, operation);

        // 按状态精确查询
        if (status != null) {
            wrapper.eq(SysLog::getStatus, status);
        }

        // 按时间范围查询
        if (startTime != null) {
            wrapper.ge(SysLog::getCreateTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(SysLog::getCreateTime, endTime);
        }

        // 按创建时间倒序排列（最新的在前面）
        wrapper.orderByDesc(SysLog::getCreateTime);

        return baseMapper.selectPage(page, wrapper);
    }
}