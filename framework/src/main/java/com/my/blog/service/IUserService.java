package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.UserDto;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.AdminUserInfoVo;

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

    void addUser(UserDto userDto);

    void deleteUserById(Long id);

    ResponseResult getUserInfoById(Long id);

    ResponseResult updateUser(UserDto userUpdateDto);
}
