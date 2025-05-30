package com.my.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserListVo {
    private Long id;
    private String userName;
    private String nickName;
    private String status;
    private String email;
    private String phonenumber;
    private String sex;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long updateBy;
}
