package com.my.blog.controller;

import com.my.blog.service.IArticleService;
import com.my.blog.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
