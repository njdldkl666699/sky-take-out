package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "购物车DTO")
public class ShoppingCartDTO implements Serializable {

    @Schema(title = "菜品ID")
    private Long dishId;
    @Schema(title = "套餐ID")
    private Long setmealId;
    @Schema(title = "菜品口味数据")
    private String dishFlavor;

}
