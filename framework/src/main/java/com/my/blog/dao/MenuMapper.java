package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.Menu;

import java.util.List;


/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author WH
 * @since 2025-06-05
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);
}
