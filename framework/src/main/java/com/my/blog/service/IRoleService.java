package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.RoleDto;
import com.my.blog.domain.entity.Role;

import java.util.List;

public interface IRoleService {
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName);

    ResponseResult changeStatus(Long roleId, String status);

    ResponseResult addRole(RoleDto roleDto);

    List<Role> listAllRole();

    ResponseResult getRoleById(Long id);

    ResponseResult updateRole(RoleDto roleDto);

    ResponseResult deleteRoleById(Long id);
}
