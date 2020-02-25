package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.pojo.Cart;

import java.util.List;

public interface CartService {
    /**
     * 查看购物车列表
     * */
    ServerResponse list(Integer userId);

    /**
     * 购物车中添加商品
     * */

    ServerResponse add(Integer userId,Integer productId,Integer count);

    /**
     * 已选中的商品
     * @return
     */
    ServerResponse checked(Integer userId);

    /**
     * 批量删除购物车
     */
    ServerResponse deleteBatchCart(List<Cart> cartList);
}
