package com.mall.mallsys.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallsys.modules.system.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户 ID 获取角色列表
     */
    List<SysRole> getRolesByUserId(Long userId);
}