package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/27日 3:28
 * ----------------------
 * ---------类描述--------
 **/
@RestController

public class OrderAdminController {
    @Autowired
    OrderService orderService;

    @GetMapping("admin/order/list")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 订单流程转台 0取消 。10未付款
     *
     * @return
     */
    @PostMapping("admin/order/deliver")
    public ApiRestResponse deliver(@RequestParam String order) {
        orderService.deliver(order);
        return ApiRestResponse.success();
    }

    /**
     * 完结订单
     *
     * @return
     */
    @PostMapping("order/finish")
    public ApiRestResponse finish(@RequestParam String order) {
        orderService.finish(order);
        return ApiRestResponse.success();
    }
}
