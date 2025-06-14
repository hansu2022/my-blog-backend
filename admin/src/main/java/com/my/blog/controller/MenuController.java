package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.MenuDto;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.vo.MenuVo;
import com.my.blog.service.IMenuService;
import com.my.blog.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;
    @GetMapping("/treeselect")
    public ResponseResult getMenuTree() {
        // 先从service拿树形结构List<Menu>
        List<Menu> menuTree = menuService.getMenuTree();

        // 转成MenuDto树
        List<MenuDto> dtoList = menuTree.stream()
                .map(MenuDto::fromMenu)
                .collect(Collectors.toList());

        return ResponseResult.okResult(dtoList);
    }

    @GetMapping("/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        // 查询所有菜单树（通常只包含“菜单”和“目录”类型）
        List<MenuVo> menus = menuService.selectMenuTreeAll();

        // 查询当前角色已分配的菜单id列表
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);

        Map<String, Object> data = new HashMap<>();
        data.put("menus", menus);
        data.put("checkedKeys", checkedKeys);

        return ResponseResult.okResult(data);
    }
    @GetMapping("/list")
    public ResponseResult list(@RequestParam(required = false) String status,
                               @RequestParam(required = false) String menuName) {
        return menuService.selectMenuList(status, menuName);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return ResponseResult.okResult(menu);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        try {
            boolean success = menuService.updateMenu(menu);
            return success
                    ? ResponseResult.okResult("修改成功")
                    : ResponseResult.errorResult(500, "修改失败");
        } catch (RuntimeException e) {
            return ResponseResult.errorResult(500, e.getMessage());
        }
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId) {
        try {
            boolean result = menuService.removeMenu(menuId);
            return result
                    ? ResponseResult.okResult("删除成功")
                    : ResponseResult.errorResult(500, "删除失败");
        } catch (RuntimeException e) {
            return ResponseResult.errorResult(500, e.getMessage());
        }
    }
}
