package com.neuedu.busines.dao;

import com.neuedu.busines.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 根据userId查询购物信息
     * */
    List<Cart> findCartByUserid(@Param("userId") Integer userId);

    /**
     * 根据userId查询已选中的购物信息
     * */
    List<Cart> findCartByUserIdChecked(@Param("userId") Integer userId);

    /**
     * 根据productId修改qualtity
     * */

    int  updateQualtityByProductId(@Param("productId")Integer productId, @Param("qualtity")Integer qualtity);


    /**
     * 统计购物车中未选中的商品数量
     * */
    int  totalCountByUnchecked(@Param("userId") Integer userId);


    /**
     * 根据用户id和商品id查询购物车中是否包含此商品
     * */

    Cart  findCartByUseridAndProductId(@Param("userId") Integer userId,
                                       @Param("productId") Integer productId);

    /**
     * 批量删除
     */

    int deleteBatchCart(@Param("cartList") List<Cart> cartList);
}
