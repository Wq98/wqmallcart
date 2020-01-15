package com.wq.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.wq.common.CookieUtils;
import com.wq.common.MD5Util;
import com.wq.common.SysResult;

import com.wq.entity.User;
import com.wq.service.UserService;

import com.wq.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName: UserController
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/27  14:57
 */

@RestController
@RequestMapping("/user")
@Api(tags = {"用户相关操作API"})
@RefreshScope
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private ShardedJedisPool pool;
    @ApiOperation(value = "从数据库获取userPhone",notes="需要传递userPhone参数")
    @PostMapping("/checkUserPhone")
    public SysResult checkUsername(String userPhone){
        int exist=userService.checkUserPhone(userPhone);
        if(exist==0){
            return SysResult.ok();
        }else{
            return SysResult.build(201,"",null);
        }
    }
    @ApiOperation(value = "注册用户",notes = "根据User对象创建用户")
    @PostMapping("save")
    public SysResult saveUser(User user){
        try {
            userService.saveUser(user);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return  SysResult.build(201,"",null);
        }
    }
    @ApiOperation(value = "用户登录",notes = "根据手机号和密码验证登录")
    @GetMapping("login")
    public SysResult login(User user, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        Integer count=3;
        ShardedJedis jedis=pool.getResource();
        User exist = userService.login(user);
            if("".equals(exist)){
                return SysResult.build(201,"",null);
            }else if(userService.getCount(exist.getUserPhone())>=count){
                return SysResult.build(202,"",null);
            }else{
                String tokenValue=jedis.get(MD5Util.md5("token"+user.getUserPhone()));
                User user1=userService.queryUserById(JwtUtil.verifyJwt(tokenValue).get("userId").toString());
               CookieUtils.setCookie(request,response,"ticket",user1.getUserPhone()+"."+tokenValue);
                return SysResult.ok();
            }
    }
    @GetMapping("query/{ticket}")
    public SysResult queryUserInfo(@PathVariable String ticket){
        String userList=userService.queryUserInfo(ticket);
        if(userList==null){
            return SysResult.build(201,"",null);
        }else{
            return SysResult.build(200,"",userList);
        }
    }


    @ApiIgnore
    @RequestMapping("logout")
    public SysResult logout(HttpServletRequest request, HttpServletResponse response){
        //删除cookie
        CookieUtils.deleteCookie(request, response, "ticket");
        return SysResult.ok();
    }
}
