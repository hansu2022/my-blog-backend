package com.my.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.MenuMapper;
import com.my.blog.dao.RoleMenuMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.entity.RoleMenu;
import com.my.blog.domain.vo.MenuVo;
import com.my.blog.service.IMenuService;

import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2025-06-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
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
       .orderByAsc(true, Menu::getParentId) // 第一个参数 true 表示升序
       .orderByAsc(true, Menu::getOrderNum); // 再次调用 orderByAsc
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

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menus = menuMapper.selectList(null);
        // 找出根菜单
        List<Menu> roots = menus.stream()
                .filter(m -> m.getParentId() != null && m.getParentId() == 0)
                .collect(Collectors.toList());

        roots.forEach(root -> buildTree(root, menus));
        return roots;
    }

    private void buildTree(Menu parent, List<Menu> allMenus) {
        List<Menu> children = allMenus.stream()
                .filter(m -> parent.getId().equals(m.getParentId()))
                .collect(Collectors.toList());
        parent.setChildren(children);
        children.forEach(child -> buildTree(child, allMenus));
    }
    public List<MenuVo> selectMenuTreeAll() {
        List<Menu> menus = menuMapper.selectList(
                new LambdaQueryWrapper<Menu>()
                        .in(Menu::getMenuType, "C", "M") // 目录和菜单
                        .eq(Menu::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                        .orderByAsc(Menu::getParentId)
                        .orderByAsc(Menu::getOrderNum)
        );

        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);

        return buildMenuTree(menuVos);
    }

    private List<MenuVo> buildMenuTree(List<MenuVo> menus) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> setChildren(menu, menus))
                .collect(Collectors.toList());
    }

    private MenuVo setChildren(MenuVo parent, List<MenuVo> menus) {
        List<MenuVo> children = menus.stream()
                .filter(menu -> menu.getParentId().equals(parent.getId()))
                .map(menu -> setChildren(menu, menus))
                .collect(Collectors.toList());
        parent.setChildren(children);
        return parent;
    }
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return roleMenuMapper.selectList(
                        new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId)
                ).stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }
    @Override
    public ResponseResult selectMenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(status)) {
            wrapper.eq(Menu::getStatus, status);
        }

        if (StringUtils.hasText(menuName)) {
            wrapper.like(Menu::getMenuName, menuName);
        }

        wrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);

        List<Menu> menus = menuMapper.selectList(wrapper);
        return ResponseResult.okResult(menus);
    }
    @Override
    public ResponseResult addMenu(Menu menu) {
        menuMapper.insert(menu);
        return ResponseResult.okResult("添加成功");
    }
    @Override
    public Menu getById(Long id) {
        return baseMapper.selectById(id);
    }
    @Override
    public boolean updateMenu(Menu menu) {
        // 校验不能将父菜单设置为自己
        if (menu.getId().equals(menu.getParentId())) {
            throw new RuntimeException("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }

        // 执行更新
        return this.updateById(menu);
    }

    @Override
    public boolean removeMenu(Long menuId) {
        // 校验该菜单是否有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menuId);
        long childCount = this.count(wrapper);
        if (childCount > 0) {
            throw new RuntimeException("删除失败：请先删除子菜单");
        }

        // 正常删除
        return this.removeById(menuId);
    }
}
