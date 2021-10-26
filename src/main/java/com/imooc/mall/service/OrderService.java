package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.vo.OrderVO;

public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNum);

    PageInfo listForCustom(Integer pageNum, Integer pageSize);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void cancel(String orderNum);

    String codeQR(String orderNo);

    void pay(String orderNo);

    void deliver(String orderNo);

    void finish(String orderNo);
}
