package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;

public interface CategoryService {
    public ServerResponse addCategory(Integer parentId, String name);

    public ServerResponse setCategory(Integer categoryId, String categoryName);

    ServerResponse getCategory(Integer categoryId);

    ServerResponse getDeepCategory(Integer categoryId);
}
