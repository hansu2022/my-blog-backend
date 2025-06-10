package com.my.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MenuVo {
    private String component;
    private LocalDateTime createTime;
    private String icon;
    private Long id;
    private String menuName;
    private String menuType;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String visible;
    private String status;
    private String perms;
    private List<MenuVo> children; // 子菜单列表
}
