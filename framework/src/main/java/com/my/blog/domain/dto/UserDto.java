package com.my.blog.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String userName;
    private String nickName;
    private String password;
    private String phonenumber;
    private String email;
    private String sex;
    private String status;
    private List<Long> roleIds;

    // 省略getter/setter
}
