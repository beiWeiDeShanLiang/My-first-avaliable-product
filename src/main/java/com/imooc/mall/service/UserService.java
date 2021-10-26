package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.User;

import java.security.NoSuchAlgorithmException;

/**
 *  UserService 接口类
 */
public interface UserService {
    User getUser();
    void register(String username,String password) throws ImoocMallException, NoSuchAlgorithmException;

    User login(String username, String password) throws ImoocMallException;

    void update(User user) throws ImoocMallException;

    boolean checkAdminRole(User user);
}
