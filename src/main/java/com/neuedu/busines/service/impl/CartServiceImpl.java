package com.neuedu.busines.service.impl;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.dao.CartMapper;
import com.neuedu.busines.pojo.Cart;
import com.neuedu.busines.service.CartService;
import com.neuedu.busines.service.ProductService;
import com.neuedu.busines.utils.BigDecimalUtil;
import com.neuedu.busines.vo.CartProductVo;
import com.neuedu.busines.vo.CartVo;
import com.neuedu.busines.vo.ProductDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductService productService;

    @Override
    public ServerResponse list(Integer userId) {
        CartVo cartVo = getCart(userId);

        return ServerResponse.serverResponseBySucess(null, cartVo);
    }

    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_NULL.getCode(), StatusEnum.PARAM_NOT_NULL.getMsg());
        }
        ServerResponse serverResponse = productService.productDetails(productId);
        if (!serverResponse.isSucess()) {
            return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getCode(), StatusEnum.PRODUCT_NOT_EXISTS.getMsg());
        }
        Cart cart = cartMapper.findCartByUseridAndProductId(userId, productId);
        if (cart == null) {
            //该商品第一次加入到购物车
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setChecked(Consts.CartProductCheckEnum.CHECKED.getCheck());
            newCart.setQuantity(count);
            newCart.setCreateTime(new Date());
            newCart.setUpdateTime(new Date());
            int insert = cartMapper.insert(newCart);
            if (insert <= 0) {
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_ADD_CART_FAIL.getCode(), StatusEnum.PRODUCT_ADD_CART_FAIL.getMsg());
            } else {
                return ServerResponse.serverResponseBySucess(null, newCart);
            }
        } else {
            //商品存在
            cart.setQuantity(cart.getQuantity() + count);
            int i = cartMapper.updateByPrimaryKey(cart);
            if (i <= 0) {
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_UPDATE_CART_FAIL.getCode(), StatusEnum.PRODUCT_UPDATE_CART_FAIL.getMsg());
            } else {
                return ServerResponse.serverResponseBySucess(null, getCart(userId));
            }
        }
    }
    //判断商品状态是否选中
    @Override
    public ServerResponse checked(Integer userId) {
        if (userId == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.USER_OUT_LOGIN.getCode(), StatusEnum.USER_OUT_LOGIN.getMsg());
        }
        List<Cart> cartByUserIdChecked = cartMapper.findCartByUserIdChecked(userId);

        return ServerResponse.serverResponseBySucess(null, cartByUserIdChecked);
    }

    @Override
    public ServerResponse deleteBatchCart(List<Cart> cartList) {
        if (cartList == null || cartList.size() == 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_NULL.getCode(), StatusEnum.PARAM_NOT_NULL.getMsg());
        }
        int i = cartMapper.deleteBatchCart(cartList);
        if (i <= 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.CART_CLEAR_FAIL.getCode(),StatusEnum.CART_CLEAR_FAIL.getMsg());
        }
        return ServerResponse.serverResponseBySucess();
    }

    private CartVo getCart(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.findCartByUserid(userId);
        if (cartList == null || cartList.size() == 0) {
            return cartVo;
        }
        List<CartProductVo> cartProductVos = new ArrayList<>();
        BigDecimal cartTotalPrice = new BigDecimal("0"); //总价
        for (Cart cart : cartList) {
            CartProductVo productVo = new CartProductVo();
            productVo.setId(cart.getId());
            productVo.setUserId(cart.getUserId());
            productVo.setProductChecked(cart.getChecked());
            productVo.setProductId(cart.getProductId());
            ServerResponse<ProductDetailsVo> serverResponse = productService.productDetails(cart.getProductId());
            if (!serverResponse.isSucess()) {
                continue;
            }
            ProductDetailsVo productDetailsVo = serverResponse.getData();
            productVo.setProductMainImage(productDetailsVo.getMainImage());
            productVo.setProductName(productDetailsVo.getName());
            productVo.setProductPrice(productDetailsVo.getPrice());
            productVo.setProductStock(productDetailsVo.getStock());
            productVo.setProductSubtitle(productDetailsVo.getSubtitle());
            Integer quantity = cart.getQuantity();
            Integer stock = productDetailsVo.getStock();

            if (stock >= quantity) {
                productVo.setLimitQuantity("LIMIT_NUM_SUCCESS");
                productVo.setQuantity(cart.getQuantity());
            } else {
                //库存不足
                //修改该商品在购物车中的数量，根据商品id修改购物车的qualtity
                int i = cartMapper.updateQualtityByProductId(productVo.getProductId(), stock);
                if (i <= 0) {
                    continue;
                }
                productVo.setLimitQuantity("LIMIT_NUM_FAIL");
                productVo.setQuantity(stock);
            }
            //价格计算
            productVo.setProductTotalPrice(BigDecimalUtil.multi(String.valueOf(productDetailsVo.getPrice().doubleValue()), String.valueOf(productVo.getQuantity())));
            if (productVo.getProductChecked() == Consts.CartProductCheckEnum.CHECKED.getCheck()) {
                cartTotalPrice = BigDecimalUtil.add(String.valueOf(cartTotalPrice.doubleValue()),
                        String.valueOf(productVo.getProductTotalPrice().doubleValue()));
            }
            cartProductVos.add(productVo);
        }
        cartVo.setCartProductVOList(cartProductVos);
        cartVo.setCartTotalPrice(cartTotalPrice);

        int i = cartMapper.totalCountByUnchecked(userId);
        if (i > 0) {
            cartVo.setAllCheck(false);
        } else {
            cartVo.setAllCheck(true);
        }
        return cartVo;
    }
}
