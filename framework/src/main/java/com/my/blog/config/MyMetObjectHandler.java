package com.my.blog.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.my.blog.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class MyMetObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId=null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId=-1L;
        }
        this.setFieldValByName("createTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("createBy", userId,metaObject);
        this.setFieldValByName("updateBy", userId,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId=null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId=-1L;
        }
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateBy", userId,metaObject);
    }
}
