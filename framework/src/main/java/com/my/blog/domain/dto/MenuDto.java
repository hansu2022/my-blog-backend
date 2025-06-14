package com.my.blog.domain.dto;

import com.my.blog.domain.entity.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDto extends Menu {
    private Long id;
    private String label;      // 前端需要的字段名，映射 Menu.menuName
    private Long parentId;
    private List<Menu> children = new ArrayList<>();
    public static MenuDto fromMenu(Menu menu) {
        MenuDto dto = new MenuDto();
        dto.setId(menu.getId());
        dto.setLabel(menu.getMenuName());
        dto.setParentId(menu.getParentId());
        // 递归转换 children
        if (menu.getChildren() != null) {
            for (Menu child : menu.getChildren()) {
                dto.getChildren().add(fromMenu(child));
            }
        }
        return dto;
    }
}
