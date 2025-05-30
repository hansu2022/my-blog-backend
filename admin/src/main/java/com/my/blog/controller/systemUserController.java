package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/user")
public class systemUserController {

    @Autowired
    private IUserService userService;

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
}
