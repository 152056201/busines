package com.neuedu.busines.controller;

import com.neuedu.busines.dao.BusinessMapper;
import com.neuedu.busines.pojo.Business;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
    @Resource
    BusinessMapper businessMapper;
    @RequestMapping("/findbyid/{id}")
    public Business findById(@PathVariable("id") Integer id){
        Business business = businessMapper.selectByPrimaryKey(id);
        return business;
    }
}
