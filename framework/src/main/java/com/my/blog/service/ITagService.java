package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author CWJ
 * @since 2025-06-10
 */
public interface ITagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(Tag tag);
}
