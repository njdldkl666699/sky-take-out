package com.sky.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * <p>订单数据</p>
     * 用于绕过支付，调用submitOrder时为其赋值，调用payment时获取订单id
     */
    private Orders lastSubmittedOrder;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //异常情况的处理（收货地址为空、超出配送范围、购物车为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 查询当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setUserId(userId);
        order.setConsignee(addressBook.getConsignee());
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        // 将订单数据赋给this的变量（是不是用BaseContext更好？）
        this.lastSubmittedOrder = order;

        // 向订单表中插入数据
        orderMapper.insert(order);

        // 订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        // 向明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetailList);

        // 清理购物车中的数据
        shoppingCartMapper.deleteByUserId(userId);

        // 封装返回结果
        return OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderTime(order.getOrderTime())
                .orderAmount(order.getAmount())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal("0.01"), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }

        // 绕过微信支付，详见：
        // https://blog.csdn.net/RuanFun/article/details/135861498?spm=1001.2014.3001.5501
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        Map<String, Object> map = new HashMap<>();
        map.put("id", lastSubmittedOrder.getId());
        map.put("status", Orders.TO_BE_CONFIRMED);  // 支付状态，已支付
        map.put("payStatus", Orders.PAID);  // 订单状态，待接单
        map.put("checkoutTime", LocalDateTime.now());   // 更新支付时间
        orderMapper.updateByMap(map);

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 历史订单查询
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult pageQuery(int pageNum, int pageSize, Integer status) {
        // 条件分页查询
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        PageHelper.startPage(pageNum, pageSize);
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        // 获取订单详细信息
        List<OrderVO> records = new ArrayList<>();

        // 处理为空
        if (page == null || page.getTotal() <= 0) {
            return new PageResult(0, records);
        }

        for (Orders orders : page) {
            Long orderId = orders.getId();
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);

            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setOrderDetailList(orderDetailList);

            records.add(orderVO);
        }

        return new PageResult(page.getTotal(), records);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO getDetail(Long id) {
        // 查询订单信息
        Orders order = orderMapper.getById(id);
        // 查询订单明细表
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 封装返回数据
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 取消订单
     *
     * @param id
     */
    @Override
    public void userCancelById(Long id) throws Exception {
        Orders orderDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 待支付和待接单状态下，用户可直接取消订单
        // 否则不能直接取消，需电话沟通商家
        // 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (orderDB.getStatus() > Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders order = new Orders();
        order.setId(id);

        // 订单处于待接单状态下取消，需要进行退款
        if (orderDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // 调用微信支付退款接口
            // 和订单支付一样，需要绕过，直接注释掉即可

//            weChatPayUtil.refund(
//                    orderDB.getNumber(),    //商户订单号
//                    orderDB.getNumber(),    //商户退款单号
//                    new BigDecimal("0.01"), //退款金额，单位 元
//                    new BigDecimal("0.01"));    //原订单金额

            //支付状态修改为 退款
            order.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason("用户取消");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    /**
     * <p>再来一单</p>
     * 业务规则：<br>
     * 再来一单就是将原订单中的商品重新加入到购物车中
     *
     * @param id
     */
    @Override
    public void repetition(Long id) {
        // 查询订单明细表
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        Long userId = BaseContext.getCurrentId();

        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(orderDetail -> {
            // 将订单明细数据转换为购物车数据
            ShoppingCart shoppingCart = new ShoppingCart();
            // 不拷贝id（含义不同）
            BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).toList();

        // 向购物车表批量插入
        shoppingCartMapper.insertBatch(shoppingCartList);
    }
}
