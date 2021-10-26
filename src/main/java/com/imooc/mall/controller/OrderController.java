package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.service.OrderService;
import com.imooc.mall.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/26日 20:24
 * ----------------------
 * ---------类描述--------
 **/
@RestController
public class OrderController {
   @Autowired
   OrderService service;

   @ApiOperation("创建订单")
   @PostMapping("order/create")
   public ApiRestResponse create(@RequestBody CreateOrderReq req){

      String orderNo = service.create(req);
      return ApiRestResponse.success(orderNo);
   }

   @ApiOperation("查询订单信息")
   @PostMapping("order/detail")
   public ApiRestResponse detail(@RequestParam String  orderNum){

      OrderVO orderVO = service.detail(orderNum);
      return ApiRestResponse.success(orderVO);
   }


   @ApiOperation("订单列表")
   @PostMapping("order/list")
   public ApiRestResponse list(@RequestParam("pageNum") Integer pageNum , @RequestParam("pageSize") Integer pageSize){
      PageInfo pageInfo = service.listForCustom(pageNum, pageSize);
      return ApiRestResponse.success(pageInfo);
   }

   @ApiOperation("取消订单")
   @PostMapping("order/cancel")
   public ApiRestResponse list(@RequestParam String orderNo ){
      service.cancel(orderNo);
      return ApiRestResponse.success();
   }

   @ApiOperation("生成支付二维码")
   @PostMapping("order/codeQR")
   public ApiRestResponse codeQR(@RequestParam String orderNo ){
      String pngAddress = service.codeQR(orderNo);
      return ApiRestResponse.success(pngAddress);
   }

   @ApiOperation("支付")
   @GetMapping("pay")
   public ApiRestResponse pay(@RequestParam String orderNo ){
      service.pay(orderNo);
      return ApiRestResponse.success();
   }
}
