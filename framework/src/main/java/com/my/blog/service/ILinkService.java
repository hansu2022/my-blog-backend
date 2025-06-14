package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Link;

/**
 * <p>
 * 友链 服务类
 * </p>
 *
 * @author CWJ
 * @since 2025-05-26
 */
public interface ILinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name);

    void addLink(Link link);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLinkById(Link link);

    void deleteLinkById(Long id);
}
