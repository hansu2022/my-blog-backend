package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.RoleDto;
import com.my.blog.domain.entity.Role;
import com.my.blog.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    public ResponseResult listRoles(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(required = false) String roleName) {
        return roleService.getRoleList(pageNum, pageSize, roleName);
    }
    @PutMapping("/changeStatu")
    public ResponseResult changeRoleStatus(@RequestBody Map<String, String> params) {
        Long roleId = Long.parseLong(params.get("roleId"));
        String status = params.get("status");
        return roleService.changeStatus(roleId, status);
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto) {
        return roleService.addRole(roleDto);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        List<Role> roles = roleService.listAllRole();
        return ResponseResult.okResult(roles);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id) {
        return roleService.deleteRoleById(id);
    }

}
