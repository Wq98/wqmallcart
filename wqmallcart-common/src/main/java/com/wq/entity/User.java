package com.wq.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName: User
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/27  14:57
 */
@Data
public class User {
    private String userId;
    private String userPhone;
    private String password;
    private String userBirth;
    private String nickname;
    private Date registerTime;
    private Integer userState;
    private String token;
}
