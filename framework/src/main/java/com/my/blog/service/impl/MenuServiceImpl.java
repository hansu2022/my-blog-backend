package com.my.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.MenuMapper;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.vo.MenuVo;
import com.my.blog.service.IMenuService;

import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

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

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus;
        //判断是否是管理员
        if(userId == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, 'C', 'M')
                    .eq(Menu::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                    .orderByAsc(Menu::getParentId, Menu::getOrderNum);
            menus = menuMapper.selectList(wrapper);
        }
        else{
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }

        // 将perm值为null的替换为空字符串
        for(Menu menu : menus){
            if(menu.getPerms() == null){
                menu.setPerms("");
            }
        }

        //做数据封装，使用MenuVo
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);

        //构建父子菜单，先找一级菜单，再把子菜单设置到一级菜单的children中
        List<MenuVo> menuTree = builderMenuTree(menuVos);

        return menuTree;
    }

    private List<MenuVo> builderMenuTree(List<MenuVo> menuVos) {
        // 先找一级菜单（parentId 为 0L 的菜单 ）
        List<MenuVo> menuTree = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(0L))
                // 把于菜单设置到一级菜单的 children 中
                .map(menuVo -> setChildrenList(menuVo, menuVos))
                .collect(Collectors.toList());
        return menuTree;
    }


    private MenuVo setChildrenList(MenuVo parent, List<MenuVo> menuVos) {
        // 从 menuVos 里面找到所有 parentId 等于 parent 的 id 的菜单，作为 parent 的子菜单
        List<MenuVo> childrenList = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());
        parent.setChildren(childrenList);
        return parent;
    }

}
