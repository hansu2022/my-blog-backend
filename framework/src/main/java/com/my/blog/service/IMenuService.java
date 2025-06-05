package com.my.blog.service;

import java.util.List;

public interface IMenuService {
    List<String> selectPermsByUserId(Long id);
}
