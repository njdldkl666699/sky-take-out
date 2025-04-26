package com.sky.service;

import com.sky.dto.SetmealDTO;

public interface SetmealService {


    /**
     * 新增套餐，同时保存相关联的菜品数据
     * @param setmealDTO
     */
    void saveWithDishes(SetmealDTO setmealDTO);
}
