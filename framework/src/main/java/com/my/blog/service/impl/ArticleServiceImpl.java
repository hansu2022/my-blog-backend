package com.my.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.entity.Article;
import com.my.blog.service.IArticleService;
import org.springframework.stereotype.Service;

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

}
