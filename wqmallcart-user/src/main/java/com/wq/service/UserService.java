package com.wq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wq.common.CookieUtils;
import com.wq.common.MD5Util;
import com.wq.common.TypeTransformMapper;
import com.wq.common.UUIDUtil;
import com.wq.entity.LoginInfo;
import com.wq.entity.User;
import com.wq.mapper.UserMapper;
import com.wq.serviceImpl.UserServiceImpl;
import com.wq.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: UserService
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/27  14:57
 */
@Service
public class UserService implements UserServiceImpl {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ShardedJedisPool pool;
    @Override
    public int checkUserPhone(String userPhone) {
        return userMapper.checkUserPhone(userPhone);
    }

    @Override
    public void saveUser(User user) {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.format(date);
        user.setUserId(UUIDUtil.getUUID());
        String md5Password= MD5Util.md5(user.getPassword());
        user.setPassword(md5Password);
        user.setRegisterTime(date);
        userMapper.saveUser(user);
    }


    @Override
    public User login(User user) throws JsonProcessingException {
        ShardedJedis jedis=pool.getResource();
        Integer sum=3;
        Integer count=0;
        String userPhone=user.getUserPhone();
        user.setPassword(MD5Util.md5(user.getPassword()));
        User userExist=userMapper.queryExist(user);
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.format(date);
        LoginInfo loginInfo=new LoginInfo();
        loginInfo.setLoginTime(date);
        loginInfo.setUserPhone(userPhone);
        loginInfo.setLoginId(UUIDUtil.getUUID().replace("-",""));
        if(userExist==null){
            count+=1;
            loginInfo.setErrorNum(count);
            loginInfo.setLoginFlag(1);
            userMapper.insertLogin(loginInfo);
            int countError =this.getCount(userPhone);
            if(countError>=sum){
                boolean b = userMapper.updateUserState(userPhone);
                return null;
            }else{
                return null;
            }
        }else{
            boolean a = userMapper.updateUserState1(userPhone);
            loginInfo.setErrorNum(count);
            loginInfo.setLoginFlag(0);
            userMapper.insertLogin(loginInfo);
            User userExist1=userMapper.queryExistStateNot1(user);
            if(userExist1!=null) {
                //token生成
                String token= JwtUtil.generateToken(userExist1.getUserId());
                jedis.setex(MD5Util.md5("token"+userExist1.getUserPhone()),60*30,token);
                return user;
            } else {
                return null;
            }
        }
    }

    @Override
    public String queryUserInfo(String ticket) {
        ShardedJedis jedis=pool.getResource();
        try {
            //根据前端传来的“令牌”转换成redis中存储的token
            String token= MD5Util.md5("token"+ticket);
            //判断token剩余时间
            Long leftTime=jedis.pttl(token);
            //取token的value值
            String info=jedis.get(token);
            Claims claims = JwtUtil.verifyJwt(info);
            User user=userMapper.queryUserInfo(claims.get("userId").toString());
            String userList= TypeTransformMapper.OM.writeValueAsString(user);
            if(user!=null){
            Long leaseTime=1000*60*2L;
            if(leftTime<=leaseTime){
                //续租
                leftTime=leftTime+1000*60*20L;
                jedis.pexpire(token,leftTime);
            }
            return userList;
            }else{

                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int getCount(String userPhone){
        int countError = userMapper.countError(userPhone);
        return countError;
    }

    public User queryUserById(String userId){
        return userMapper.queryUserById(userId);
    }
}
