package com.my.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IArticleService;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @GetMapping("/hotArticleList")
    @ResponseBody
    public ResponseResult hotArticleList() {
        return articleService.hotArticleList();
    }
    @GetMapping("/articleList")
    @ResponseBody
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        ResponseResult result = articleService.articleList(pageNum, pageSize, categoryId);
        return result;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }
}
