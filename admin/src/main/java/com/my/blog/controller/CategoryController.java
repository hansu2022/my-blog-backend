package com.my.blog.controller;


import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/list")
    public ResponseResult articleList(@RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize,
                                      @RequestParam(defaultValue = "0") Long categoryId) {
        return categoryService.getCategoryList(pageNum, pageSize, categoryId);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id) {
        CategoryVo categoryVo = categoryService.getCategoryById(id);
        return ResponseResult.okResult(categoryVo);
    }
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return ResponseResult.okResult();
    }
    @PutMapping()
    public ResponseResult upCategoryById(@RequestBody Category updateDto) {
        // 调用Service，传id和更新内容
        return categoryService.upCategoryById(updateDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseResult.okResult("操作成功");
    }

}
