package com.my.blog.runner;

import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.domain.entity.Article;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    RedisCache  redisCache;

    @Override
    public void run(String... args) throws Exception {
        List<Article> articles = articleMapper.selectList(null);
        HashMap<String, Object> hashMap = new HashMap<>();
        articles.stream().map(article -> hashMap.put(article.getId().toString(), article.getViewCount().intValue()))
                .collect(Collectors.toList());
        redisCache.setCacheMap(SystemConstants.VIEW_COUNT, hashMap);
    }

}
