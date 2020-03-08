package com.neuedu.busines.service.impl;

import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.dao.ShippingMapper;
import com.neuedu.busines.pojo.Shipping;
import com.neuedu.busines.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Shipping shipping) {
        int insert = shippingMapper.insert(shipping);
        if (insert <= 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_ADD_FAIL.getCode(), StatusEnum.ADDRESS_ADD_FAIL.getMsg());
        }
        return ServerResponse.serverResponseBySucess(null, shipping.getId());
    }
}
