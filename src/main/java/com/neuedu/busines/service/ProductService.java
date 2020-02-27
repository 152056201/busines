package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.pojo.Product;

public interface ProductService {
    ServerResponse saveProduct(Product product);

    ServerResponse listProduct(Integer categoryId,String keyword,Integer pageNum,Integer pageSize,String orderby);

    ServerResponse productDetails(Integer id);

    ServerResponse updateStock(Integer productId,Integer quantity,int type);
}
