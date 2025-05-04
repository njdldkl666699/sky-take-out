package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 根据时间段统计营业数据
     *
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     *
     * @return
     */
    OrderOverViewVO orderOverView();

    /**
     * 查询菜品总览
     *
     * @return
     */
    DishOverViewVO overviewDishes();

    /**
     * 查询套餐总览
     *
     * @return
     */
    SetmealOverViewVO overviewSetmeals();
}
