package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 向订单表插入数据
     *
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号和用户id查询订单
     *
     * @param orderNumber
     * @param userId
     * @return
     */
    @Select("select * from orders where number = #{orderNumber} and user_id = #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * <p>根据map更新订单信息</p>
     * 绕过微信支付使用<br>
     * id - 订单id<br>
     * status - 订单状态<br>
     * payStatus - 支付状态<br>
     * checkoutTime - 结账时间<br>
     *
     * @param map
     */
    void updateByMap(Map<String, Object> map);

    /**
     * 分页查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 通过订单id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);
}
