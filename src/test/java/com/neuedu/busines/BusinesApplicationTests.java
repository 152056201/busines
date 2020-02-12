package com.neuedu.busines;

import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class BusinesApplicationTests {
    @Autowired
    UserService userService;
    @Test
    void insert() {
        User user= new User();
        user.setUsername("yuanhao");
        user.setPassword("123456");
        user.setEmail("1767977142@qq.com");
        user.setPhone("18902187323");
        user.setQuestion("xxxx");
        user.setAnswer("xxxx");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userService.resgister(user);
    }

}
