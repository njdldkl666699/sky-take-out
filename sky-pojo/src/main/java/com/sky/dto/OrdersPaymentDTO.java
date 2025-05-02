package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "订单支付DTO")
public class OrdersPaymentDTO implements Serializable {

    @Schema(title = "订单号")
    private String orderNumber;

    @Schema(title = "付款方式")
    private Integer payMethod;

}
