package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    /**
     * 生成token
     */
    ServerResponse generateToken();

    /**
     * 校验token
     */
    ServerResponse checkToken(HttpServletRequest request);
}
