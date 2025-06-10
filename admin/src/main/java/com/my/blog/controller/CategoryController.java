package com.my.blog.controller;


import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        List<CategoryVo> categoryVoList = categoryService.listAllCategory();
        return ResponseResult.okResult(categoryVoList);
    }
}
