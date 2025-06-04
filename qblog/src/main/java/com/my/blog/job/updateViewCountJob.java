package com.my.blog.job;

import com.my.blog.constant.SystemConstants;
import com.my.blog.domain.entity.Article;
import com.my.blog.service.impl.ArticleServiceImpl;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class updateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleServiceImpl articleServiceImpl;

    @Scheduled(cron = "0 */5 * * * *")
    public void updateViewCount() {
        Map<String, Object> cacheMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
        List<Article> collect = cacheMap.entrySet().stream()
                .map(article -> new Article(Long.valueOf(article.getKey().toString()), Long.valueOf(article.getValue().toString())))
                .collect(Collectors.toList());
        articleServiceImpl.updateBatchById(collect);
    }
}
