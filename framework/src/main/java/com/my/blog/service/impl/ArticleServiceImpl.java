package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.dto.AddArticleDto;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.ArticleTag;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.ArticleListVo;
import com.my.blog.domain.vo.HotArticleVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.IArticleService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IArticleTagService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.my.blog.domain.vo.ArticleDetailVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IArticleTagService articleTagService;

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

        Integer cacheMapValue = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT, id.toString());
        articleDetailVo.setViewCount(Long.valueOf(cacheMapValue));
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.addCacheMapValue(SystemConstants.VIEW_COUNT, id.toString(), 1);
        return null;
    }

    @Override
    public ResponseResult getAdminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(title != null, Article::getTitle, title)
                .like(summary != null, Article::getSummary, summary)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);

        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        articleMapper.selectPage(articlePage, queryWrapper);
        List<Article> articles = articlePage.getRecords();

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addArticle(AddArticleDto addArticleDto) {
        // 添加博文
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);

        // 添加博文 - tag 的关联关系
        // List<Long> -> List<ArticleTag>
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }
}
