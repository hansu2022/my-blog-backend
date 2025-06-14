package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.RoleMapper;
import com.my.blog.dao.UserMapper;
import com.my.blog.dao.UserRoleMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.UserDto;
import com.my.blog.domain.entity.Role;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.entity.UserRole;
import com.my.blog.domain.vo.AdminUserInfoVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.domain.vo.UserInfoVo;
import com.my.blog.domain.vo.UserListVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IUserService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

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
    @Override
    public void addUser(UserDto userDto) {
        // 1. 转换UserDto到User实体
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        // 2. 处理密码加密、角色关联等业务逻辑
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 3. 保存用户
        userMapper.insert(user);

        // 4. 处理角色关联（示例）
        if (userDto.getRoleIds() != null) {
            for (Long roleId : userDto.getRoleIds()) {
                userRoleMapper.insert(new UserRole(user.getId(), roleId));
            }
        }
    }
    @Override
    public void deleteUserById(Long id) {
        userMapper.deleteById(id);
        // 如果有用户角色关联表，也需要删除对应记录
        userRoleMapper.deleteByUserId(id);
    }
    @Override
    public ResponseResult getUserInfoById(Long id) {
        // 查询用户信息
        User user = userMapper.selectById(id);
        if (user == null) {
            return ResponseResult.errorResult(404, "用户不存在");
        }

        // 查询所有角色列表
        List<Role> roles = roleMapper.selectList(null);
//
        // 查询该用户关联的角色ID列表
        List<Long> roleIds = userRoleMapper.selectList(
                        new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());

        // 封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("roles", roles);
        data.put("roleIds", roleIds);

        return ResponseResult.okResult(data);
    }
    @Override
    public ResponseResult updateUser(UserDto dto) {
        // 必须传 userName
        if (!StringUtils.hasText(dto.getUserName())) {
            return ResponseResult.errorResult(400, "用户名不能为空");
        }

        // 根据 userName 查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, dto.getUserName());
        User dbUser = userMapper.selectOne(wrapper);

        if (dbUser == null) {
            return ResponseResult.errorResult(404, "用户不存在");
        }

        Long id = dbUser.getId();

        // 执行用户信息更新
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setId(id); // 确保 ID 被设置

        int updateCount = userMapper.updateById(user);
        if (updateCount == 0) {
            return ResponseResult.errorResult(500, "更新失败");
        }

        // 删除旧角色关联
        userRoleMapper.deleteByUserId(id);

        // 添加新角色关联
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            List<UserRole> userRoles = dto.getRoleIds().stream()
                    .map(roleId -> new UserRole(id, roleId))
                    .collect(Collectors.toList());
            userRoleMapper.batchInsert(userRoles);
        }

        return ResponseResult.okResult("更新成功");
    }
}
