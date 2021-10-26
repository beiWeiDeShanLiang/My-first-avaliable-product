package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdAndProductId(Integer userId,Integer productId);

    List<CartVO> selectList(@Param("userId")Integer userId);

    Integer selectedOrNot(@Param("userId")Integer userId,@Param("productId")Integer product,@Param("selected")Integer selected);

}