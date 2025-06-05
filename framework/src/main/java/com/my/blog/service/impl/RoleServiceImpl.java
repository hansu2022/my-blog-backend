package com.my.blog.service.impl;


import com.my.blog.dao.RoleMapper;
import com.my.blog.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2025-06-05
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
     if (id == 1L){
         ArrayList<String> roles = new ArrayList<>();
         roles.add("admin");
         return roles;
     }
     return roleMapper.selectRoleKeyByUserId(id);
    }
}
