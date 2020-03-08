package com.neuedu.busines.controller;

import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token/")
public class TokenController {
    @Autowired
    TokenService tokenService;

    /**
     * 生成token
     *
     * @return
     */
    @RequestMapping("getToken")
    public ServerResponse generateToken() {
        return tokenService.generateToken();
    }
}
