package com.neuedu.busines.service.impl;

import com.neuedu.busines.common.Consts;
import com.neuedu.busines.common.ServerResponse;
import com.neuedu.busines.common.StatusEnum;
import com.neuedu.busines.dao.OrderItemMapper;
import com.neuedu.busines.dao.OrderMapper;
import com.neuedu.busines.pojo.Cart;
import com.neuedu.busines.pojo.Order;
import com.neuedu.busines.pojo.OrderItem;
import com.neuedu.busines.service.CartService;
import com.neuedu.busines.service.OrderService;
import com.neuedu.busines.service.ProductService;
import com.neuedu.busines.utils.BigDecimalUtil;
import com.neuedu.busines.utils.DateUtil;
import com.neuedu.busines.vo.OrderItemVO;
import com.neuedu.busines.vo.OrderVO;
import com.neuedu.busines.vo.ProductDetailsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    public ServerResponse createOrder(Integer userId, Integer shoppingId) {
        if (shoppingId == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_NULL.getCode(), StatusEnum.PARAM_NOT_NULL.getMsg());
        }
        //购物车中已选的商品
        ServerResponse<List<Cart>> checked = cartService.checked(userId);
        if (!checked.isSucess()) {
            return checked;
        }
        List<Cart> cartList = checked.getData();
        if (cartList == null || cartList.size() == 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.CART_NULL.getCode(), StatusEnum.CART_NULL.getMsg());
        }
        ServerResponse<List<OrderItem>> serverResponse = assemableOrderItemList(cartList, userId);
        if (!serverResponse.isSucess()) {
            return serverResponse;
        }
        //生成订单 并插入到数据库
        List<OrderItem> orderItems = serverResponse.getData();
        ServerResponse<Order> serverResponse1 = generatorOrder(userId, orderItems, shoppingId);
        if (!serverResponse1.isSucess()) {
            return serverResponse1;
        }
        Order order = serverResponse1.getData();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        int i = orderItemMapper.insertBatch(orderItems);
        if (i <= 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.ORDERITEM_CREATE_FAIL.getCode(), StatusEnum.ORDERITEM_CREATE_FAIL.getMsg());
        }
        //扣库存
        reduceStock(orderItems);
        ServerResponse serverResponse2 = clearCart(cartList);
        if (!serverResponse2.isSucess()) {
            return serverResponse2;
        }
        //8.前端返回OrderVO

        OrderVO orderVO= assemableOrderVO(order,orderItems,shoppingId);
        return ServerResponse.serverResponseBySucess(null,orderVO);
    }

    private ServerResponse assemableOrderItemList(List<Cart> cartList, Integer userId) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cart : cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cart.getProductId());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setUserId(userId);
            //根据商品ID查询商品信息
            ServerResponse<ProductDetailsVo> productDetails = productService.productDetails(cart.getProductId());
            if (!productDetails.isSucess()) {
                return productDetails;
            }
            //商品是否处于在售状态
            ProductDetailsVo detailsData = productDetails.getData();
            if (detailsData.getStatus() == Consts.ProductStatusEnum.PRODUCT_OFFLINE.getStatus()) {
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getCode(), StatusEnum.PRODUCT_NOT_EXISTS.getMsg());
            }
            //判断商品库存是否充足
            if (detailsData.getStock() < detailsData.getStock()) {
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_ENOUGH.getCode(), StatusEnum.PRODUCT_NOT_ENOUGH.getMsg());
            }
            orderItem.setProductName(detailsData.getName());
            orderItem.setCurrentUnitPrice(detailsData.getPrice());
            orderItem.setProductImage(detailsData.getMainImage());
            orderItem.setTotalPrice(BigDecimalUtil.multi(String.valueOf(detailsData.getPrice().doubleValue()), String.valueOf(cart.getQuantity())));
            orderItems.add(orderItem);
        }
        return ServerResponse.serverResponseBySucess(null, orderItems);
    }

    /**
     * 生成订单
     *
     * @return
     */
    private ServerResponse generatorOrder(Integer userId, List<OrderItem> orderItems, Integer shoppingId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingId(shoppingId);
        order.setPayment(getTotalPrice(orderItems)); //总金额
        order.setPaymentType(1);
        order.setPostage(0);
        order.setStatus(Consts.OrderStatusEnum.UNPAY.getStatus());
        order.setOrderNo(createOrderId());
        //将订单落库
        int insert = orderMapper.insert(order);
        if (insert > 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_CREATE_FAIL.getCode(), StatusEnum.ORDER_CREATE_FAIL.getMsg());
        }
        return ServerResponse.serverResponseBySucess(null, order);
    }

    /**
     * 计算金额
     */
    private BigDecimal getTotalPrice(List<OrderItem> orderItems) {
        BigDecimal total = new BigDecimal("0");
        for (OrderItem orderItem : orderItems) {
            total = BigDecimalUtil.multi(String.valueOf(total.doubleValue()), String.valueOf(orderItem.getCurrentUnitPrice()));
        }
        return total;
    }

    /**
     * 生成订单号
     */
    private long createOrderId() {
        return System.currentTimeMillis();
    }

    /**
     * 商品减库存
     */
    private ServerResponse reduceStock(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Integer productId = orderItem.getProductId();
            Integer quantity = orderItem.getQuantity();
            //根据商品ID减库存
            ServerResponse serverResponse = productService.reduceStock(productId, quantity);
            if (!serverResponse.isSucess()) {
                return serverResponse;
            }
        }
        return ServerResponse.serverResponseBySucess();
    }

    /**
     * 清空购物车下单的商品
     */
    private ServerResponse clearCart(List<Cart> cartList) {
        ServerResponse serverResponse = cartService.deleteBatchCart(cartList);
        return serverResponse;
    }

    private OrderVO assemableOrderVO(Order order,List<OrderItem> orderItems,Integer shippingId) {
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setStatus(order.getStatus());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentTime(DateUtil.date2Str(order.getPaymentTime()));
        orderVO.setSendTime(DateUtil.date2Str(order.getSendTime()));
        orderVO.setCloseTime(DateUtil.date2Str(order.getCloseTime()));
        orderVO.setCreateTime(DateUtil.date2Str(order.getCreateTime()));
        orderVO.setShippingId(shippingId);

        List<OrderItemVO> orderItemVOList=new ArrayList<>();

        for(OrderItem orderItem:orderItems){
            OrderItemVO orderItemVO=convertOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }
    /**
     * orderItem-->orderItemvo
     * */
    private OrderItemVO convertOrderItemVO(OrderItem orderItem){
        if(orderItem==null){
            return null;
        }
        OrderItemVO orderItemVO=new OrderItemVO();

        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItem.setProductId(orderItem.getProductId());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        orderItemVO.setCreateTime(DateUtil.date2Str(orderItem.getCreateTime()));


        return orderItemVO;
    }
}
