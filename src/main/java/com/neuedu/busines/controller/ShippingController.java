package com.neuedu.busines.controller;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.pojo.Shipping;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.ShippingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    ShippingService shippingService;
    @RequestMapping("add.do")
    public ServerResponse addShipping(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Consts.USER);
        shipping.setUserId(user.getId());
        return shippingService.add(shipping);
    }
}
