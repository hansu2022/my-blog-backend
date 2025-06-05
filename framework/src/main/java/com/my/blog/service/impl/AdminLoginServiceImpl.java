package com.my.blog.service.impl;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.LoginUser;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.BlogUserLoginVo;
import com.my.blog.domain.vo.UserInfoVo;
import com.my.blog.service.IAdminLoginService;
import com.my.blog.utils.JwtUtil;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements IAdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //生成token 返回前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //将用户信息存入redis
        redisCache.setCacheObject("adminlogin:" + userId, loginUser);

        HashMap<Object, Object> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }
}
