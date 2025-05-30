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
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IUserService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import sun.security.util.Password;

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
    @Autowired
    PasswordEncoder  passwordEncoder;

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

    @Override
    public ResponseResult updateUserInfo(User user) {
        userMapper.updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        if (user.getNickName().isEmpty()) {
            throw new SystemException( AppHttpCodeEnum.USERNAME_NOT__NULL);
        }
        if (user.getEmail().isEmpty()) {
            throw new SystemException( AppHttpCodeEnum.EMAIL_NOT__NULL);
        }
        if (emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        userMapper.insert(user);
        return ResponseResult.okResult();
    }
    private boolean emailExist(String email) {
       return userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, email))>0;
    }
    private boolean userNameExist(String userName) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUserName, userName))>0;
    }

}
