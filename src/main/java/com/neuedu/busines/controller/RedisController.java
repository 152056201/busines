package com.neuedu.busines.controller;

import com.neuedu.busines.common.RedisApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

@RestController
public class RedisController {
    @Resource
    private RedisApi redisApi;
    @RequestMapping("redis")
    public String set(){
        String set = redisApi.set("yuanhao", "hello");
        return set;
    }
}

