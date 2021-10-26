package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNum(String orderNum);

    List<Order>  selectForCustom(Integer userId);


    List<Order>  selectForAdmin();
}