package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.UserDto;
import com.my.blog.domain.entity.Role;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.AdminUserInfoVo;
import com.my.blog.service.IRoleService;
import com.my.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class systemUserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    public ResponseResult listUsers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String phonenumber,
            @RequestParam(required = false) String status
    ) {
        return userService.getUserList(pageNum, pageSize, userName, phonenumber, status);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return ResponseResult.okResult("新增成功");
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseResult.okResult("操作成功");
    }
    @GetMapping("/{id}")
    public ResponseResult getUserInfoById(@PathVariable Long id) {
        return userService.getUserInfoById(id);
    }
    @PutMapping
    public ResponseResult updateUser(@RequestBody UserDto userUpdateDto) {
        return userService.updateUser(userUpdateDto);
    }
    // 获取全部角色列表

}
