package com.my.blog.constant;

public class SystemConstants {
    /**
     * 文章状态：草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    public static final int ARTICLE_STATUS_NORMAL = 0;


    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * redis缓存文章浏览量key
     */
    public static final String VIEW_COUNT = "viewCount";

    /**
     * 分类状态：正常
     */
    public static final String NORMAL = "0";

}
