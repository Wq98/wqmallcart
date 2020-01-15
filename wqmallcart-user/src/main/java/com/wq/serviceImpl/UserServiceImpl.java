package com.wq.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wq.entity.User;
import com.wq.mapper.UserMapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @Title: UserServiceImpl
 * @Description:
 * @Author: 魏秦
 * @Date: 2020/1/6  13:43
 */

public interface UserServiceImpl {
    /***
     * 校验注册账号是否存在
     * @param userPhone
     * @return
     */

    int checkUserPhone(String userPhone);

    /***
     * 保存用户注册信息
     * @param user
     */

    void saveUser(User user);

    /***
     * 用户登录
     * @param user
     * @return
     */

    User login(User user) throws JsonProcessingException;

    /***
     * 查询用户登录状态
     * @param ticket
     * @return
     */

    String queryUserInfo(String ticket);

    /***
     * 收集用户密码输入错误次数
     * @param userPhone
     * @return
     */
    int getCount(String userPhone);
}
