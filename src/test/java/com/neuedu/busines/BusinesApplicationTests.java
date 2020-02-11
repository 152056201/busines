package com.neuedu.busines;

import com.neuedu.busines.dao.BusinessMapper;
import com.neuedu.busines.pojo.Business;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BusinesApplicationTests {
    @Resource
    BusinessMapper businessMapper;
    @Test
    void insert() {
        Business business = new Business();
        business.setBusinessId(1);
        business.setBusinessName("aa");
        businessMapper.insert(business);
    }
    @Test
    void selectById(){
        Business business = businessMapper.selectByPrimaryKey(1);
        System.out.println(business.toString());
    }
}
