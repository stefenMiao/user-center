package com.yupi.usercenter.service;

import com.yupi.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author steafen
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-08-01 08:47:57
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
     long userRegister(String userAccount, String userPassword, String checkPassword);

     /**
      * 用户登录
      * @param userAccount 用户账号
      * @param userPassword 用户密码
      * @return 脱敏后的用户信息
      */
     User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
