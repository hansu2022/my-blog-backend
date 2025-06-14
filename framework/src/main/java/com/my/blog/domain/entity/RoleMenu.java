package com.my.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("sys_role_menu")
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenu {
    private Long roleId;
    private Long MenuId;
}
