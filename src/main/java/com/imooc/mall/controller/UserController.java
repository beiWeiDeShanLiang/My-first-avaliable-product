package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import com.mysql.cj.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * 用户控制器
 */
@Controller
@ResponseBody
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("getUser")
    public User personalPage(){

        return userService.getUser();
    }

    /**
     * 注册
     * @param username
     * @param password
     * @return
     * @throws ImoocMallException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("register")
    public ApiRestResponse register(@RequestParam("username")String username ,@RequestParam("password") String password) throws ImoocMallException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(username)){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_USERNAME));
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_PASSWORD));

        }
        //校验密码
        if(password.length()<8){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT));
        }

        userService.register(username,password);
        return ApiRestResponse.success();
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param session
     * @return
     * @throws ImoocMallException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("login")
    public ApiRestResponse login(@RequestParam("username")String username , @RequestParam("password") String password, HttpSession session) throws ImoocMallException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(username )){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_USERNAME));
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_PASSWORD));

        }
        //校验密码
        if(password.length()<8){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT));
        }

        User login = userService.login(username, password);
        //保存用户信息不保存密码
        login.setPassword(null);
        session.setAttribute(Constant.MALL_USER,login);
        return ApiRestResponse.success(login);
    }

    /**
     * 更新个性签名
     * @param session
     * @param signature
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("user/update")
    public ApiRestResponse updateUserInfo(HttpSession session,@RequestParam String signature) throws ImoocMallException {
        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser==null){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_LOGIN));
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.update(user);
        return ApiRestResponse.success();
    }

    /**
     * 登出
     * @param session
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("user/logout")
    public ApiRestResponse logout(HttpSession session) throws ImoocMallException {
      session.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param session
     * @return
     * @throws ImoocMallException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("adminLogin")
    public ApiRestResponse adminLogin(@RequestParam("username")String username , @RequestParam("password") String password, HttpSession session) throws ImoocMallException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(username )){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_USERNAME));
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_PASSWORD));

        }
        //校验密码
        if(password.length()<8){
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT));
        }

        User login = userService.login(username, password);
        if (!userService.checkAdminRole(login)) {
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NOT_ADMIN));
        }
        //保存用户信息不保存密码
        login.setPassword(null);
        session.setAttribute(Constant.MALL_USER,login);
        return ApiRestResponse.success(login);
    }

}
