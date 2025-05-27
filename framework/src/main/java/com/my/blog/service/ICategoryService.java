package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
public interface ICategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
