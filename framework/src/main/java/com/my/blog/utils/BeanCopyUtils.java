package com.my.blog.utils;

import java.lang.annotation.Target;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <T> T copyBean (Object source, Class<T> target) {
        //创建目标对象
        T result = null;
        try {
            result = target.newInstance();
            //实现属性copy
            BeanUtils.copyProperties (source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }

    public static <O,T> List<T> copyBeanList (List<O> source, Class<T> target) {
        return  source.stream()
                .map(o -> copyBean(o, target))
                .collect(Collectors.toList());
    }
}
