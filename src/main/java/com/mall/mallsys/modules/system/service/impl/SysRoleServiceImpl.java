package com.mall.mallsys.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallsys.modules.system.entity.SysRole;
import com.mall.mallsys.modules.system.mapper.SysRoleMapper;
import com.mall.mallsys.modules.system.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }
}