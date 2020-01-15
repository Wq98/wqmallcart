package com.wq.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName: LoginInfo
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/27  14:57
 */
@Data
public class LoginInfo {
    private String loginId;
    private String userPhone;
    private Integer loginFlag;
    private Integer errorNum;
    private Date loginTime;
}
