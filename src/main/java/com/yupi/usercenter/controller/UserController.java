package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.model.domain.request.UserLoginRequest;
import com.yupi.usercenter.model.domain.request.UserRegisterRequest;
import com.yupi.usercenter.service.UserService;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yupi.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.yupi.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 用户请求类
     * @return
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        Long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }

    /**
     * 通过HTTP GET请求获取当前用户的安全信息
     * 此方法首先从会话中获取登录用户对象，如果会话中不存在登录用户，则返回null
     * 如果会话中存在登录用户，将从数据库中加载该用户的信息，并返回一个安全的用户视图
     *
     * @param request HTTP请求对象，用于获取会话信息
     * @return 如果会话中没有登录用户，返回null；否则返回一个安全的用户对象
     */
    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        // 从会话中获取登录用户状态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        // 强制类型转换，将对象转换为User类型
        User currentUser = (User) userObj;
        // 检查当前是否有用户登录
        if (currentUser == null) {
            return null;
        }
        // 获取当前用户的ID
        Long userId = currentUser.getId();
        // TODO 校验用户是否合法
        // 根据用户ID从数据库中获取用户信息
        User user = userService.getById(userId);
        // 返回一个安全的用户视图
        return userService.getSafetyUser(user);
    }


    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return user;
    }

    @PostMapping("/logout")
    public Integer userLogout (HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return  userService.userLogout(request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        // 判断是否为管理员
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request) {
        // 判断是否为管理员
        if (!isAdmin(request)){
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 判断是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
