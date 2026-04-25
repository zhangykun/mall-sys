package com.mall.mallsys.modules.system.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单 VO
 */
public class MenuVO implements Serializable {

    private Long id;

    private Long parentId;

    private String name;

    private String path;

    private String component;

    private String permission;

    private Integer type;

    private String icon;

    private Integer sort;

    private List<MenuVO> children;

    // 手动添加 getter/setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getComponent() { return component; }
    public void setComponent(String component) { this.component = component; }
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public List<MenuVO> getChildren() { return children; }
    public void setChildren(List<MenuVO> children) { this.children = children; }
}
