package com.mall.mallsys.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.mallsys.modules.system.entity.SysMenu;
import com.mall.mallsys.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户权限列表
     */
    @Select("SELECT DISTINCT m.permission FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.permission IS NOT NULL")
    List<String> selectPermissionsByUserId(Long userId);

    /**
     * 查询用户菜单列表
     */
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.visible = 1 " +
            "ORDER BY m.sort ASC")
    List<SysMenu> selectMenusByUserId(Long userId);
}