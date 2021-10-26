package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.service.CartService;
import com.imooc.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/26日 16:41
 * ----------------------
 * ---------类描述--------
 **/
@Service

public class CartServiceImpl implements CartService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;
    @Override

    public List<CartVO> add(Integer userId, Integer productId, Integer count){
        validProduct(productId,count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart==null) {
            //这个商品之前不在购物车内,新增一条记录
            cart=new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Integer.parseInt(Constant.Cart.select));
            cartMapper.insertSelective(cart);
        }else{
            cart.setQuantity(count+cart.getQuantity());
            cart.setSelected(Integer.parseInt(Constant.Cart.select));
            cartMapper.updateByPrimaryKey(cart);

        }
        return list(userId);
    }

    @Override
    public List<CartVO> list(Integer userId)
    {
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        cartVOS.forEach(cartVO -> {
            cartVO.setTotalPrice(cartVO.getQuantity()*cartVO.getPrice());
        });
        return cartVOS;
    }

    @Override

    public List<CartVO> update(Integer userId, Integer productId, Integer count){
        validProduct(productId,count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart==null) {
            //这个商品之前不在购物车内,报错
          throw new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAIL);
        }else{
            cart.setQuantity(count);
            cart.setSelected(Integer.parseInt(Constant.Cart.select));
            cartMapper.updateByPrimaryKey(cart);

        }
        return list(userId);
    }
    @Override

    public List<CartVO> delete(Integer userId, Integer productId){
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart==null) {
            //这个商品之前不在购物车内,报错
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }else{
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return list(userId);
    }


    @Override
    public List<CartVO> selectOrNot(Integer userId,Integer productId,Integer selected){
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAIL);
        }else {
            Integer integer = cartMapper.selectedOrNot(userId, productId, selected);
            System.out.println("更新条数"+integer);
            return this.list(userId);
        }

    }
    @Override
    public List<CartVO> selectAllOrNot(Integer userId,Integer selected){
        cartMapper.selectedOrNot(userId,null,selected);
        return list(userId);

    }



    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //判断商品是否存在且上架
        if (product ==null||!product.getStatus().equals(Constant.SALE_STATUS))
        {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ON_SALE);
        }
        if (count>product.getStock())
        {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);

        }
    }
}
