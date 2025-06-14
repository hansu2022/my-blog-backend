package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.ArticleListVo;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.IArticleService;
import com.my.blog.service.ICategoryService;
import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired


    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_COMMENT);
        List<Article> articles = articleService.list(queryWrapper);

        Set<Long> categoryIds = articles.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        List<Category> categories = categoryMapper.selectBatchIds(categoryIds);

        List<CategoryVo> categoryVos = new ArrayList<>();
        for (Category category : categories) {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            categoryVos.add(categoryVo);
        }
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public void addCategory(Category category) {
        // 这里可以做一些校验，比如分类名不能为空、不能重复等
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new RuntimeException("分类名不能为空");
        }

        // 可加查重逻辑，这里简单示例
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, category.getName());
        Integer count = Math.toIntExact(categoryMapper.selectCount(queryWrapper));
        if (count > 0) {
            throw new RuntimeException("分类名已存在");
        }

        // 新增
        categoryMapper.insert(category);
    }

    @Override
    public ResponseResult getCategoryList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 构建分页对象
        Page<Category> page = new Page<>(pageNum, pageSize);

        // 构建查询条件，按需过滤分类ID，如果categoryId为0则不过滤
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if (categoryId != 0) {
            queryWrapper.eq(Category::getId, categoryId);
        }

        // 执行分页查询
        Page<Category> categoryPage = categoryMapper.selectPage(page, queryWrapper);

        // 将Category实体转换成CategoryVo列表
        List<CategoryVo> categoryVos = categoryPage.getRecords().stream().map(category -> {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).collect(Collectors.toList());

        // 构造分页数据对象，包含rows和total
        Map<String, Object> data = new HashMap<>();
        data.put("rows", categoryVos);
        data.put("total", categoryPage.getTotal());

        return ResponseResult.okResult(data);
    }
    @Override
    public ResponseResult upCategoryById(Category updateDto) {
        Category category = categoryMapper.selectById(updateDto.getId());
        if (category == null) {
            return ResponseResult.errorResult(404, "分类不存在");
        }

        // 赋值更新字段
        category.setDescription(updateDto.getDescription());
        category.setName(updateDto.getName());
        category.setStatus(updateDto.getStatus());

        int updateCount = categoryMapper.updateById(category);
        if (updateCount > 0) {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(category, vo);
            return ResponseResult.okResult(vo);
        } else {
            return ResponseResult.errorResult(500, "更新失败");
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        int result = categoryMapper.deleteById(id);
        if (result == 0) {
            throw new RuntimeException("删除失败，分类不存在");
        }
    }
    @Override
    public CategoryVo getCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        CategoryVo vo = new CategoryVo();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }

}
