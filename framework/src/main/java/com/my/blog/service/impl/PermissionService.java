package com.my.blog.service.impl;

import com.my.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    //写一个方法，判断用户是否具备所要求的权限标识
    public boolean hasPermission(String permission){
        //如果是超级管理员，直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则，查询用户所具备的权限标识
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        if(permissions == null){
            return false;
        }
        return permissions.contains(permission);
    }
}
