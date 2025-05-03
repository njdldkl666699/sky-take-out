package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "拒单DTO")
public class OrdersRejectionDTO implements Serializable {

    @Schema(title = "订单id")
    private Long id;

    @Schema(title = "订单拒绝原因")
    private String rejectionReason;

}
