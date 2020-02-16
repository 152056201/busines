package com.neuedu.busines;

import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.dao.ProductMapper;
import com.neuedu.busines.pojo.Category;
import com.neuedu.busines.pojo.Product;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.CategoryService;
import com.neuedu.busines.service.ProductService;
import com.neuedu.busines.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
class BusinesApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @Resource
    ProductMapper productMapper;
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
    @Test
    void addCate(){
        Category category = new Category();
        category.setParentId(10001);
        category.setName("小米");
        categoryService.addCategory(category.getParentId(),category.getName());
    }
    @Test
    void addPro(){
        Product product = new Product();
        product.setName("小米10");
        product.setPrice(BigDecimal.valueOf(2000.00));
        product.setStock(400);
        product.setCategoryId(77);
        product.setSubtitle("小米10");
        product.setDetail("很好的手机");
        productService.saveProduct(product);
    }
}
