package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.service.CartService;
import com.imooc.mall.vo.CartVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/26日 16:11
 * ----------------------
 * ---------类描述--------
 **/
@Controller
@ResponseBody
@RequestMapping("cart")
public class CartController {
    @Autowired
    CartService cartService;

    @ApiOperation("购物车列表")
    @GetMapping("list")
    public ApiRestResponse list(  ) {
        //内部获取用户ID,防止横向越权
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> list = cartService.list(userId);
        return ApiRestResponse.success(list);
    }

    @ApiOperation("添加商品到购物车")
    @PostMapping("add")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.add(userId, productId, count);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("更新商品到购物车")
    @PostMapping("update")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.update(userId, productId, count);
        return ApiRestResponse.success(cartVOS);
    }


    @ApiOperation("删除商品 购物车")
    @PostMapping("delete")
    public ApiRestResponse delete(@RequestParam Integer productId) {
        //不能传入userID
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.delete(userId, productId);
        return ApiRestResponse.success(cartVOS);
    }


    @ApiOperation("选择/不选择商品 购物车")
    @PostMapping("select")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        //不能传入userID
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.selectOrNot(userId, productId,selected);
        return ApiRestResponse.success(cartVOS);
    }


    @ApiOperation("全选择/全不选商品 购物车")
    @PostMapping("selectAllOrNot")
    public ApiRestResponse selectAllOrNot( @RequestParam Integer selected) {
        //不能传入userID
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.selectAllOrNot(userId,selected);
        return ApiRestResponse.success(cartVOS);
    }
}
