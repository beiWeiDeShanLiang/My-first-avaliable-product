package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import com.imooc.mall.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24:3:16
 *
 * UserService 实现类
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUser() {
        User user = userMapper.selectByPrimaryKey(1);
        return user;
    }



    public void register(String username, String password) throws ImoocMallException, NoSuchAlgorithmException {
        User user = userMapper.selectByName(username);
        if (user!=null){
            throw new ImoocMallException(ImoocMallExceptionEnum.FORBID_SAME_NAME);
        }
        User user1 = new User();
        user1.setUsername(username);
        user1.setPassword(MD5Utils.getMd5Str(password+ Constant.salt));
        int i = userMapper.insertSelective(user1);
        if (i==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED );
        }

    }
    @Override
    public User login(String username, String password) throws ImoocMallException {
        String md5Str=null;
        try {
          md5Str = MD5Utils.getMd5Str(password+Constant.salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(username, md5Str);
        if(user==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        return  user;
    }

    @Override
    public void update(User user) throws ImoocMallException {
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i>1){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user){
        boolean equals = user.getRole().equals(Constant.ADMIN);
        return equals;
    }

}
