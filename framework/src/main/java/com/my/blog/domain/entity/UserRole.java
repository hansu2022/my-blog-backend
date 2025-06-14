package com.my.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("sys_user_role")
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private Long roleId;
    private Long userId;
}
