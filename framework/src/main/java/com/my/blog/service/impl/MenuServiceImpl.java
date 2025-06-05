package com.my.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.MenuMapper;
import com.my.blog.domain.entity.Menu;
import com.my.blog.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2025-06-05
 */
@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    MenuMapper menuMapper;
    @Override
    public List<String> selectPermsByUserId(Long id) {
       if (id == 1L){
           LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
           //ARTICLE_STATUS_NORMAL
           wrapper.in(Menu::getMenuType, "C", "F")
                   .eq(Menu::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
           List<Menu> menus = menuMapper.selectList(wrapper);
           List<String> perms = menus.stream()
                   .map(Menu::getPerms)
                   .collect(Collectors.toList());
           return perms;
       }
       return menuMapper.selectPermsByUserId(id);
    }
}
