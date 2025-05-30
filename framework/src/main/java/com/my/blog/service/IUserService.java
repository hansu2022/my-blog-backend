package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
public interface IUserService extends IService<User> {

    ResponseResult userInfo(Long userId);

    ResponseResult getUserList(int pageNum, int pageSize, String userName, String phonenumber, String status);

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}
