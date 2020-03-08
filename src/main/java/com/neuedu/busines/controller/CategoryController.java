package com.neuedu.busines.controller;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.RoleEnum;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.pojo.User;
import com.neuedu.busines.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("/add_category.do")
    public ServerResponse addCate(@RequestParam(value = "parentId", required = false) Integer parentId,
                                  @RequestParam("name") String name, HttpSession session) {
        User user = (User) session.getAttribute(Consts.USER);
        if (user.getRole() != RoleEnum.ADMIN.getRole()) {
            return ServerResponse.serverResponseByFail(StatusEnum.NO_AUTHORITY.getCode(), StatusEnum.NO_AUTHORITY.getMsg());
        }
        ServerResponse serverResponse = categoryService.addCategory(parentId, name);
        return serverResponse;
    }

    @RequestMapping("/set_category_name.do")
    public ServerResponse setCategoryName(@RequestParam("categoryId") Integer categoryId,
                                          @RequestParam("categoryName") String categoryName,
                                          HttpSession session) {
        User user = (User) session.getAttribute(Consts.USER);
        if (user.getRole() != RoleEnum.ADMIN.getRole()) {
            return ServerResponse.serverResponseByFail(StatusEnum.NO_AUTHORITY.getCode(), StatusEnum.NO_AUTHORITY.getMsg());
        }
        ServerResponse serverResponse = categoryService.setCategory(categoryId, categoryName);
        return serverResponse;
    }

    @RequestMapping("/get_category.do")
    public ServerResponse getCategory(@RequestParam("categoryId") Integer categoryId) {
        ServerResponse category = categoryService.getCategory(categoryId);
        return category;
    }

    @RequestMapping("/get_deep_category.do")
    public ServerResponse getDeepCategory(@RequestParam("categoryId") Integer categoryId) {
        ServerResponse deepCategory = categoryService.getDeepCategory(categoryId);
        return deepCategory;
    }
}
