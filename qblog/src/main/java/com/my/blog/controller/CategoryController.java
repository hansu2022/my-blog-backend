package com.my.blog.controller;

import com.my.blog.service.ICategoryService;
import com.my.blog.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ResponseBody
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }
}
