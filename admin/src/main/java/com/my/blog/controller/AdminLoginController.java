package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.AdminUserInfoVo;
import com.my.blog.domain.vo.UserInfoVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IAdminLoginService;
import com.my.blog.service.IMenuService;
import com.my.blog.service.IRoleService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminLoginController {
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAdminLoginService adminLoginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
//提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    @GetMapping("/getI nfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
//获取当前登录的用户
        User user = SecurityUtils.getLoginUser().getUser();
//根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());
//根据用户id查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(user.getId());
//获取用户信息
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
//封装数据返回
        AdminUserInfoVo adminUserInfoVo = new
                AdminUserInfoVo(perms,roles,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
}
