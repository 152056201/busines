package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;

public interface OrderService {
    /**
     * 创建订单
     * @param userId
     * @param shoppingId
     * @return
     */
    ServerResponse createOrder(Integer userId,Integer shoppingId);
    /**
     * 取消订单
     */
    ServerResponse cancel(Long orderNO);
}
