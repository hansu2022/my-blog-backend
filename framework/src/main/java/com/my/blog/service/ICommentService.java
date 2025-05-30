package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Comment;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
public interface ICommentService extends IService<Comment> {

    ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pagesize);

    ResponseResult addComment(Comment comment);
}
