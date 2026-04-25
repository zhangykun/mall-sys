package com.mall.mallsys.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long parentId;
    private String name;
    private String path;
    private String component;
    private String permission;
    private Integer type;
    private String icon;
    private Integer sort;
    private Integer visible;

    @TableField(exist = false)
    private List<SysMenu> children;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
