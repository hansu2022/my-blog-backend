package com.my.blog.service;

import com.my.blog.domain.vo.MenuVo;

import java.util.List;

public interface IMenuService {
    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);
}
