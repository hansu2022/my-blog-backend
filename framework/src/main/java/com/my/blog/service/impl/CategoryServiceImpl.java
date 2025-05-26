package com.my.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.entity.Category;
import com.my.blog.service.ICategoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
