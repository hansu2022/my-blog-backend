package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.entity.Tag;
import com.my.blog.dao.TagMapper;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author CWJ
 * @since 2025-06-10
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 构造查询条件
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        // 分页查询
        Page<Tag> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);

        page(page, queryWrapper);

        // 封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        if(!StringUtils.hasText(tag.getName())){
            if(!StringUtils.hasText(tag.getName())){
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
            }
        }
        tagMapper.insert(tag);
        return ResponseResult.okResult("标签添加成功");
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        if(Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        tagMapper.deleteById(id);
        return ResponseResult.okResult("标签删除成功");
    }

    @Override
    public ResponseResult getTag(Long id) {
        if(Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Tag tag = tagMapper.selectById(id);
        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        if(!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        tagMapper.updateById(tag);
        return ResponseResult.okResult("标签更新成功");
    }
}
