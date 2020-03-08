package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;

public interface OrderService {
    /**
     * 创建订单
     *
     * @param userId
     * @param shoppingId
     * @return
     */
    ServerResponse createOrder(Integer userId, Integer shoppingId);

    /**
     * 取消订单
     */
    ServerResponse cancel(Long orderNO);

    /**
     * 根据订单号查询订单信息
     */
    ServerResponse findOrderByOrderNo(Long orderNo);

    /**
     * 支付成功后，修改订单状态
     */
    ServerResponse updateOrder(Long orderNo, String payTime, Integer orderStatus);
}
