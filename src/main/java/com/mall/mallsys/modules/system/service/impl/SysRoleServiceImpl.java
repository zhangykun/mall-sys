package com.mall.mallsys.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallsys.modules.system.entity.SysRole;
import com.mall.mallsys.modules.system.mapper.SysRoleMapper;
import com.mall.mallsys.modules.system.mapper.SysRoleMenuMapper;
import com.mall.mallsys.modules.system.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    public SysRoleServiceImpl(SysRoleMenuMapper sysRoleMenuMapper) {
        this.sysRoleMenuMapper = sysRoleMenuMapper;
    }

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return sysRoleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        // 先清空旧关联
        sysRoleMenuMapper.deleteByRoleId(roleId);
        // 批量插入新关联
        if (menuIds != null && !menuIds.isEmpty()) {
            sysRoleMenuMapper.batchInsert(roleId, menuIds);
        }
    }
}