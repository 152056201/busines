package com.neuedu.busines.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.dao.ProductMapper;
import com.neuedu.busines.pojo.Product;
import com.neuedu.busines.service.CategoryService;
import com.neuedu.busines.service.ProductService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryService categoryService;

    @Override
    public ServerResponse saveProduct(Product product) {
        if (product == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_NULL.getCode(), StatusEnum.PARAM_NOT_NULL.getMsg());
        }
        Integer productId = product.getId();
        String subImage = product.getSubImages();
        if (subImage != null && subImage.length() > 0) {
            String subImages = subImage.split(",")[0];
            product.setMainImage(subImages);
        }
        if (productId == null) { //添加商品
            int insert = productMapper.insert(product);
            if (insert <= 0) {
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_ADD_FAIL.getCode(), StatusEnum.PRODUCT_ADD_FAIL.getMsg());
            } else {
                return ServerResponse.serverResponseBySucess("添加成功", null);
            }
        } else { //商品更新
            Product product1 = productMapper.selectByPrimaryKey(productId); //先进行查询
            if (product1 == null) {
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getCode(), StatusEnum.PRODUCT_NOT_EXISTS.getMsg());
            }
            int i = productMapper.updateByPrimaryKey(product);
            if (i <= 0) {
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_UPDATE_FAIL.getCode(), StatusEnum.PRODUCT_UPDATE_FAIL.getMsg());
            } else {
                return ServerResponse.serverResponseBySucess("商品更新成功", product);
            }
        }

    }

    @Override
    public ServerResponse listProduct(Integer categoryId, String keyword, Integer pageSize, Integer pageNum, String orderby) {
        if (categoryId == -1 && (keyword == null || keyword.equals(""))) {
            PageHelper.startPage(pageNum,pageSize);
            List<Product> productList = new ArrayList<>();
            PageInfo pageInfo = new PageInfo(productList);
            return ServerResponse.serverResponseBySucess("",pageInfo);
        }

        List<Integer> listCategoryIds = new ArrayList<>();
        if (categoryId != -1) {
            ServerResponse<Set<Integer>> deepCategory = categoryService.getDeepCategory(categoryId);
            System.out.println(deepCategory.toString());
            if (deepCategory.isSucess()) {
                Set<Integer> setCategoryIds = deepCategory.getData();
                Iterator<Integer> iterator = setCategoryIds.iterator();
                while (iterator.hasNext()){
                    listCategoryIds.add(iterator.next());
                }
            }
            System.out.println(listCategoryIds);
        }
        if (keyword != null && !keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        PageHelper.startPage(pageNum,pageSize);

        List<Product> byCategoryIdKeyWord = productMapper.findByCategoryIdKeyWord(listCategoryIds, keyword);
        PageInfo pageInfo = new PageInfo(byCategoryIdKeyWord);

        return ServerResponse.serverResponseBySucess("",pageInfo);
    }
}
