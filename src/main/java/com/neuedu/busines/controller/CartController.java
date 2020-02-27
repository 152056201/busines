package com.neuedu.busines.controller;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session) {
        User user = (User) session.getAttribute(Consts.USER);
        return cartService.list(user.getId());
    }

    @RequestMapping("add.do")
    public ServerResponse add(Integer productId, Integer count, HttpSession session) {
        User user = (User) session.getAttribute(Consts.USER);
        return cartService.add(user.getId(), productId, count);
    }
}
