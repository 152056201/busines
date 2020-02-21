package com.neuedu.busines.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.dao.ProductMapper;
import com.neuedu.busines.pojo.Product;
import com.neuedu.busines.service.CategoryService;
import com.neuedu.busines.service.ProductService;

import com.neuedu.busines.utils.DateUtil;
import com.neuedu.busines.vo.ProductDetailsVo;
import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Service;
import com.neuedu.busines.vo.ProductVoList;

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
    public ServerResponse listProduct(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderby) {
        if (categoryId == -1 && (keyword == null || keyword.equals(""))) {
            //pageNum = (pageNum-1)*pageSize;
            PageHelper.startPage(pageNum, pageSize);
            List<Product> productList = new ArrayList<>();
            PageInfo pageInfo = new PageInfo(productList);
            return ServerResponse.serverResponseBySucess("", pageInfo);
        }

        List<Integer> listCategoryIds = new ArrayList<>();
        if (categoryId != -1) {
            ServerResponse<Set<Integer>> deepCategory = categoryService.getDeepCategory(categoryId);
            System.out.println(deepCategory.toString());
            if (deepCategory.isSucess()) {
                Set<Integer> setCategoryIds = deepCategory.getData();
                Iterator<Integer> iterator = setCategoryIds.iterator();
                while (iterator.hasNext()) {
                    listCategoryIds.add(iterator.next());
                }
            }
        }
        if (keyword != null && !keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        if (orderby != null && !orderby.equals("")) {
            String[] orders = orderby.split("_");
            orderby = orders[0] + " " + orders[1];
            PageHelper.orderBy(orderby);
        }

        List<Product> byCategoryIdKeyWord = productMapper.findByCategoryIdKeyWord(listCategoryIds, keyword, pageSize, pageNum, orderby);
        List<ProductVoList> productVoLists = new ArrayList<>();
        for (Product p : byCategoryIdKeyWord) {
            ProductVoList productVoList = new ProductVoList();
            productVoList.setId(p.getId());
            productVoList.setName(p.getName());
            productVoList.setCategoryId(p.getCategoryId());
            productVoList.setStatus(p.getStatus());
            productVoList.setPrice(p.getPrice());
            productVoList.setSubtitle(p.getSubtitle());
            productVoLists.add(productVoList);
            productVoList.setMainImage(p.getMainImage());
        }
        PageInfo pageInfo = new PageInfo(productVoLists);
        return ServerResponse.serverResponseBySucess("", pageInfo);
    }

    @Override
    public ServerResponse productDetails(Integer pid) {
        if (pid == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_NULL.getCode(), StatusEnum.PARAM_NOT_NULL.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(pid);
        if (product == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getCode(), StatusEnum.PRODUCT_NOT_EXISTS.getMsg());
        }
        ProductDetailsVo productDetailsVo = productDetailsVo(product);
        return ServerResponse.serverResponseBySucess("null", productDetailsVo);
    }

    private ProductDetailsVo productDetailsVo(Product product) {
        ProductDetailsVo vo = new ProductDetailsVo();
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setMainImage(product.getMainImage());
        vo.setStatus(product.getStatus());
        vo.setSubImages(product.getSubImages());
        vo.setPrice(product.getPrice());
        vo.setSubtitle(product.getSubtitle());
        vo.setDetails(product.getDetail());
        vo.setUpdateTime(product.getUpdateTime());
        return vo;
    }
}
