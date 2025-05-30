package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.UserMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.domain.vo.UserInfoVo;
import com.my.blog.domain.vo.UserListVo;
import com.my.blog.service.IUserService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(Long userId) {
        Long userId2 = SecurityUtils.getUserId();
        User user = userMapper.selectById(userId);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(user, UserInfoVo.class));
    }


    @Override
    public ResponseResult getUserList(int pageNum, int pageSize, String userName, String phonenumber, String status) {
        Page<User> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), User::getStatus, status);
        wrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        wrapper.like(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber);

        userMapper.selectPage(page, wrapper);

        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);
        return ResponseResult.okResult(new PageVo(userListVos, page.getTotal()));
    }
}
