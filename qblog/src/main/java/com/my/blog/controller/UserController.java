package com.my.blog.controller;

import com.my.blog.config.SecurityConfig;
import com.my.blog.dao.UserMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;
import com.my.blog.service.IUserService;
import com.my.blog.utils.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(@RequestParam("userId") Long userId) {
        return userService.userInfo(userId);
    }
    @PutMapping("/userInfo")
    @ResponseBody
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    @ResponseBody
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
