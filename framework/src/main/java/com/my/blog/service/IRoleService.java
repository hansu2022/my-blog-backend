package com.my.blog.service;

import java.util.List;

public interface IRoleService {
    List<String> selectRoleKeyByUserId(Long id);
}
