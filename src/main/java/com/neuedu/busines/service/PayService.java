package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;

import java.util.Map;

public interface PayService {
    ServerResponse pay(Long orderNo);

    String callbackLogic(Map<String, String> signParam);
}
