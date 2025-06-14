package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;

import java.util.List;

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

    List<CategoryVo> listAllCategory();

    void addCategory(Category category);

    ResponseResult getCategoryList(Integer pageNum, Integer pageSize, Long categoryId);



    void deleteCategoryById(Long id);

    ResponseResult upCategoryById(Category updateDto);

    CategoryVo getCategoryById(Long id);


//    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);
}
