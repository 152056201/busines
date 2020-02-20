package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;

public interface CartService {
    /**
     * 查看购物车列表
     * */
    ServerResponse list(Integer userId);

    /**
     * 购物车中添加商品
     * */

    ServerResponse add(Integer userId,Integer productId,Integer count);
}
