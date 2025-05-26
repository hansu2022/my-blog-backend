package com.my.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.LinkMapper;
import com.my.blog.entity.Link;
import com.my.blog.service.ILinkService;
import org.springframework.stereotype.Service;

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

}
