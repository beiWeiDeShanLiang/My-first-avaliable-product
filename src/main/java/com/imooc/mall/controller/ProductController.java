package com.imooc.mall.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/25日 20:50
 * ----------------------
 * ---------类描述--------
 * 前台商品controller
 **/
@Controller
@ResponseBody
public class ProductController {

    @Autowired
    ProductService productService;



    @ApiOperation("前台商品详情")
    @PostMapping("product/detail")
    public ApiRestResponse detail(@RequestBody String strId)
    {
        int id = Integer.parseInt(JSON.parseObject(strId).get("id").toString());
        Product detail = productService.detail(id);
        return ApiRestResponse.success(detail);

    }

    @ApiOperation("前台商品列表")
    @PostMapping("product/productList")
    public ApiRestResponse list(@RequestBody ProductListReq productListReq)
    {

        PageInfo detail = productService.list(productListReq);
        return ApiRestResponse.success(detail);

    }
}
