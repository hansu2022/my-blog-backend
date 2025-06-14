package com.my.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.RoleMapper;
import com.my.blog.dao.RoleMenuMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.RoleDto;
import com.my.blog.domain.entity.Role;
import com.my.blog.domain.entity.RoleMenu;
import com.my.blog.service.IRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2025-06-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
     if (id == 1L){
         ArrayList<String> roles = new ArrayList<>();
         roles.add("admin");
         return roles;
     }
     return roleMapper.selectRoleKeyByUserId(id);
    }
    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName) {
        Page<Role> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(roleName)) {
            queryWrapper.like(Role::getRoleName, roleName);
        }

        Page<Role> rolePage = roleMapper.selectPage(page, queryWrapper);

        List<Role> voList = rolePage.getRecords().stream().map(role -> {
            Role vo = new Role();
            BeanUtils.copyProperties(role, vo);
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("rows", voList);
        data.put("total", rolePage.getTotal());

        return ResponseResult.okResult(data);
    }
    // 修改角色状态
    public ResponseResult changeStatus(Long roleId, String status) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return ResponseResult.errorResult(404, "角色不存在");
        }
        role.setStatus(status);
        roleMapper.updateById(role);
        return ResponseResult.okResult("状态修改成功");
    }

    // 新增角色
    @Override
    @Transactional
    public ResponseResult addRole(RoleDto roleDto) {
        // 1. 新建角色对象
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);

        // 2. 保存角色
        roleMapper.insert(role);

//        // 3. 保存角色菜单关联（role_menu）
//        List<Long> menuIds = roleDto.getMenuIds();
//        if (menuIds != null && !menuIds.isEmpty()) {
//            List<RoleMenu> roleMenus = menuIds.stream().map(menuId -> {
//                RoleMenu rm = new RoleMenu();
//                rm.setRoleId(role.getId());  // 自动生成的ID
//                rm.setMenuId(menuId);
//                return rm;
//            }).collect(Collectors.toList());
//
//            roleMenuMapper.insertBatchSomeColumn(roleMenus); // 或者手动循环插入
//        }

        return ResponseResult.okResult("角色新增成功");
    }

    @Override
    public List<Role> listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, "0"); // 只查询启用状态的角色
        return list(queryWrapper); // list() 是 ServiceImpl 提供的
    }
    @Override
    public ResponseResult getRoleById(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            return ResponseResult.errorResult(404, "角色不存在");
        }
        return ResponseResult.okResult(role);
    }
    @Transactional
    @Override
    public ResponseResult updateRole(RoleDto roleDto) {
        // 1. 更新角色表信息
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        int rows = roleMapper.updateById(role);
        if (rows == 0) {
            return ResponseResult.errorResult(500, "更新角色失败");
        }

        Long roleId = roleDto.getId();

        // 2. 删除旧的角色菜单关联
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);

        // 3. 添加新的菜单关联
        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                .map(menuId -> {
                    RoleMenu rm = new RoleMenu();
                    rm.setRoleId(roleId);
                    rm.setMenuId(menuId);
                    return rm;
                }).collect(Collectors.toList());

        if (!roleMenus.isEmpty()) {
            for (RoleMenu rm : roleMenus) {
                roleMenuMapper.insert(rm);
            }
        }

        return ResponseResult.okResult("更新成功");
    }
    @Transactional
    @Override
    public ResponseResult deleteRoleById(Long id) {
        // 1. 删除角色菜单关联
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);

        // 2. 删除角色
        int rows = roleMapper.deleteById(id);
        if (rows == 0) {
            return ResponseResult.errorResult(404, "角色不存在或已删除");
        }

        return ResponseResult.okResult("删除成功");
    }
}
