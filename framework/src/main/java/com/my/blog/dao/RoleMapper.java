package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.Role;

import java.util.List;


/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author WH
 * @since 2025-06-05
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
