package com.neuedu.busines.service;

import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.pojo.Shipping;

public interface ShippingService {
    ServerResponse add(Shipping shipping);
}
