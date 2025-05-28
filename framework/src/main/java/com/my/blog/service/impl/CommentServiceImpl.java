package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.CommentMapper;
import com.my.blog.dao.UserMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Comment;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.CommentVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.ICommentService;
import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;


    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pagesize) {
        //取根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<Comment>()
        .eq(Comment::getArticleId, articleId)
        .eq(Comment::getRootId, -1L)
        .orderByDesc(Comment::getCreateTime);

        //分页查询
        Page<Comment> commentPage = new Page<>(pageNum, pagesize);
        commentMapper.selectPage(commentPage, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(commentPage.getRecords());

        //取子评论
        commentVoList.forEach(commentVo -> {
        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>()
            .eq(Comment::getRootId, commentVo.getId())
            .orderByDesc(Comment::getCreateTime));
            commentVo.setChildren(toCommentVoList(comments));
        });

        return ResponseResult.okResult(new PageVo(commentVoList, commentPage.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(comment.getContent().isEmpty()){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        commentMapper.insert(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> toCommentVoList(List<Comment> records) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(records, CommentVo.class);
        commentVos.forEach(commentVo -> {
            User user = userMapper.selectById(commentVo.getCreateBy());
            if(user != null){
                commentVo.setUsername(user.getNickName());
            }
            User user1 = userMapper.selectById(commentVo.getToCommentUserId());
            if(user1 != null){
                commentVo.setToCommentUserName(user1.getNickName());
            }

        });
        return commentVos;
    }
}
