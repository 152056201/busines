package com.neuedu.busines.controller;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 订单
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @RequestMapping("/create.do")
    public ServerResponse create(Integer shoppingId,HttpSession session){
        User user =  (User) session.getAttribute(Consts.USER);
        if(user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_OUT_LOGIN.getCode(),StatusEnum.USER_OUT_LOGIN.getMsg());
        }
        return orderService.createOrder(user.getId(),shoppingId);
    }
}
