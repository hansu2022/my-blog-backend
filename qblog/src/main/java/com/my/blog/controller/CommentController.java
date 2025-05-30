package com.my.blog.controller;

import com.my.blog.constant.SystemConstants;
import com.my.blog.domain.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.ICommentService;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @GetMapping("/commentList")
    @ResponseBody
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId, pageNum, pageSize);
    }

    @PostMapping()
    @ResponseBody
    public ResponseResult addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ResponseBody
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return
                commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }

}
