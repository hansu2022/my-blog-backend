package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.vo.MenuVo;

import java.util.List;

public interface IMenuService {
    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> getMenuTree();

    List<MenuVo> selectMenuTreeAll();

    List<Long> selectMenuListByRoleId(Long roleId);

    ResponseResult selectMenuList(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    Menu getById(Long id);


    boolean updateMenu(Menu menu);

    boolean removeMenu(Long menuId);
}
