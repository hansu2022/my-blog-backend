package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.LinkMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Link;
import com.my.blog.domain.vo.LinkVo;
import com.my.blog.service.ILinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_COMMENT);
        List<Link> links = linkMapper.selectList(queryWrapper);
        //封装VO
        List<LinkVo> linkVos = new ArrayList<>();
        for (Link link : links) {
            LinkVo linkVo = new LinkVo();
            BeanUtils.copyProperties(link, linkVo);
            linkVos.add(linkVo);
        }
        return ResponseResult.okResult(linkVos);
    }
    @Override
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name) {
        Page<Link> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            queryWrapper.like(Link::getName, name);
        }

        Page<Link> linkPage = linkMapper.selectPage(page, queryWrapper);

        List<LinkVo> voList = linkPage.getRecords().stream().map(link -> {
            LinkVo vo = new LinkVo();
            BeanUtils.copyProperties(link, vo);
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("rows", voList);
        data.put("total", linkPage.getTotal());

        return ResponseResult.okResult(data);
    }
    @Override
    public void addLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException("链接信息不能为空");
        }

        if (link.getName() == null || link.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("名称不能为空");
        }

        if (link.getAddress() == null || link.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("地址不能为空");
        }


        if (link.getStatus() != null) {
            List<String> validStatus = Arrays.asList("0", "1", "2");
            if (!validStatus.contains(link.getStatus())) {
                throw new IllegalArgumentException("状态码不合法，必须为0、1或2");
            }
        }

        // 其他字段校验可视业务需求扩展
        linkMapper.insert(link);
    }
    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = linkMapper.selectById(id);
        if (link == null) {
            return ResponseResult.errorResult(404, "链接不存在");
        }

        // 可选：转换成VO对象
        LinkVo vo = new LinkVo();
        BeanUtils.copyProperties(link, vo);

        return ResponseResult.okResult(vo);
    }
    @Override
    public ResponseResult updateLinkById(Link link) {
        if (link == null || link.getId() == null) {
            return ResponseResult.errorResult(400, "链接ID不能为空");
        }

        Link existing = linkMapper.selectById(link.getId());
        if (existing == null) {
            return ResponseResult.errorResult(404, "链接不存在");
        }

        // 这里可以做字段校验，简单示例：
        if (link.getName() == null || link.getName().trim().isEmpty()) {
            return ResponseResult.errorResult(400, "名称不能为空");
        }
        if (link.getAddress() == null || link.getAddress().trim().isEmpty()) {
            return ResponseResult.errorResult(400, "地址不能为空");
        }
        // 状态校验
        List<String> validStatus = Arrays.asList("0", "1", "2");
        if (link.getStatus() != null && !validStatus.contains(link.getStatus())) {
            return ResponseResult.errorResult(400, "状态码不合法");
        }

        // 执行更新
        int updateCount = linkMapper.updateById(link);
        if (updateCount > 0) {
            return ResponseResult.okResult("更新成功");
        } else {
            return ResponseResult.errorResult(500, "更新失败");
        }
    }
    @Override
    public void deleteLinkById(Long id) {
        int result = linkMapper.deleteById(id);
        if (result == 0) {
            throw new RuntimeException("删除失败，链接不存在或已被删除");
        }
    }
}
