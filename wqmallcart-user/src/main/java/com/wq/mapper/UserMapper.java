package com.wq.mapper;


import com.wq.entity.LoginInfo;
import com.wq.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: UserMapper
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/27  14:57
 */
public interface UserMapper {
    /***
     * 判断注册框中手机号码是否已经注册过
     * @param userPhone
     * @return
     */
    int checkUserPhone(@Param("userPhone") String userPhone);

    /***
     * 注册成功将用户信息以user对象存入数据库
     * @param user
     */
    void saveUser(User user);

    /***
     * 用户登录时根据手机号和密码判断是否存在该用户
     * @param user
     * @return
     */
    User queryExist(User user);

    /***
     * 将用户登录信息如登录时间、密码输入错误次数等存入登录信息表
     * @param loginInfo
     */
    void insertLogin(LoginInfo loginInfo);

    /***
     * 统计密码24小时内输入错误次数
     * @param userPhone
     * @return
     */
    int countError(String userPhone);

    /***
     * 更新用户状态值，不为1才能登录，否则将会被锁定24小时
     * @param userPhone
     * @return
     */
    boolean updateUserState(String userPhone);

    /***
     * 更新用户状态值，不为1才能登录，否则将会被锁定24小时
     * @param userPhone
     * @return
     */
    boolean updateUserState1(String userPhone);

    /***
     * 查询用户状态不为1
     * @param user
     * @return
     */
    User queryExistStateNot1(User user);

    /***
     * 根据手机号码查询用户信息
     * @param userId
     * @return
     */
    User queryUserInfo(String userId);

    /***
     * 根据手机号码查询用户信息
     * @param userPhone
     * @return
     */
    User queryUserPhone(String userPhone);

    /***
     * 根据手机号码查询用户信息
     * @param userId
     * @return
     */
    User queryUserById(String userId);
}
