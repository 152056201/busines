package com.neuedu.busines.service.impl;

import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.dao.CategoryMapper;
import com.neuedu.busines.pojo.Category;
import com.neuedu.busines.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(Integer parentId, String name) {
        if (parentId == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARENTID_NOT_NULL.getCode(), StatusEnum.PARENTID_NOT_NULL.getMsg());
        }
        if (name == null && name.equals("")) {
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORYNAME_NOT_NULL.getCode(), StatusEnum.CATEGORYNAME_NOT_NULL.getMsg());
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(name);
        int insert = categoryMapper.insert(category);
        if(insert<0){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_INSERT_FAIL.getCode(),StatusEnum.CATEGORY_INSERT_FAIL.getMsg());
        }
        return ServerResponse.serverResponseBySucess();
    }

    @Override
    public ServerResponse setCategory(Integer categoryId, String categoryName) {
        if(categoryId==null){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_NOT_FIND.getCode(),StatusEnum.CATEGORY_NOT_FIND.getMsg());
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category==null){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_NOT_FIND.getCode(),StatusEnum.CATEGORY_NOT_FIND.getMsg());
        }
        category.setName(categoryName);
        int i = categoryMapper.updateByPrimaryKey(category);
        if(i<0){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORY_UPDATE_FAIL.getCode(),StatusEnum.CATEGORY_UPDATE_FAIL.getMsg());
        }
        return ServerResponse.serverResponseBySucess();
    }

    @Override
    public ServerResponse getCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORYID_NOT_NULL.getCode(),StatusEnum.CATEGORYID_NOT_NULL.getMsg());
        }
        List<Category> categoryList = categoryMapper.getSubCategorysById(categoryId);
        return ServerResponse.serverResponseBySucess(null,categoryList);
    }

    @Override
    public ServerResponse getDeepCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByFail(StatusEnum.CATEGORYID_NOT_NULL.getCode(),StatusEnum.CATEGORYID_NOT_NULL.getMsg());
        }
        Set<Integer> set = new HashSet<>();
        Set<Integer> resultset = findAllSubCategory(set,categoryId);
        return ServerResponse.serverResponseBySucess(null,resultset);
    }

    private Set<Integer> findAllSubCategory(Set<Integer> categoryIds,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categoryIds.add(category.getId());
        }
        ServerResponse<List<Category>> serverResponse = getCategory(categoryId);
        if(!serverResponse.isSucess()){
            return categoryIds;
        }
        List<Category> categoryList = serverResponse.getData();
        for(Category c: categoryList){
            findAllSubCategory(categoryIds,c.getId());
        }
        return categoryIds;
    }
}
