package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "接单DTO")
public class OrdersConfirmDTO implements Serializable {

    @Schema(title = "订单id")
    private Long id;

    //订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
    @Schema(title = "订单状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;

}
