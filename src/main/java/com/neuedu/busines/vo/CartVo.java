package com.neuedu.busines.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVo {
    private boolean allCheck; //是否全选
    private BigDecimal cartTotalPrice;//购物车总价格
    private List<CartProductVo> cartProductVOList;

}
