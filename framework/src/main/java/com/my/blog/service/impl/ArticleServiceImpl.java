package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.ArticleListVo;
import com.my.blog.domain.vo.HotArticleVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.IArticleService;
import com.my.blog.domain.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.my.blog.domain.vo.ArticleDetailVo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_COMMENT);
        // 按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 只查询前10条
        Page<Article> page = new Page<>(1, 10);
        articleMapper.selectPage(page, queryWrapper);
        List<Article> articles = page.getRecords();

        // Bean属性拷贝
        List<HotArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article, vo);
            articleVos.add(vo);
        }

        return ResponseResult.okResult(articleVos);

    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //要求:①只能查询正式发布的文章 ②置顶的文章要显示在最前面
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_COMMENT).orderByDesc(Article::getIsTop).eq(categoryId != 0, Article::getCategoryId, categoryId);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        articleMapper.selectPage(page, queryWrapper);
        List<Article> articles = page.getRecords();

        //封装ArticleListVo
        List<ArticleListVo> articleListVos = new ArrayList<>();
        for (Article article : articles) {
            ArticleListVo articleListVo = new ArticleListVo();
            BeanUtils.copyProperties(article, articleListVo);
            // 根据categoryId查询categoryName
            Category category = categoryMapper.selectById(article.getCategoryId());
            String name = category.getName();
            articleListVo.setCategoryName(name);
            articleListVos.add(articleListVo);
        }
        //封装PageVo
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);

        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article, articleDetailVo);

        //根据分类id查询分类名
        Category category = categoryMapper.selectById(article.getCategoryId());
        articleDetailVo.setCategoryName(category.getName());

        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }
}
