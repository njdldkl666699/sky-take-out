package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(title = "用户下单DTO")
public class OrdersSubmitDTO implements Serializable {

    @Schema(title = "地址簿id")
    private Long addressBookId;

    @Schema(title = "付款方式")
    private int payMethod;

    @Schema(title = "备注")
    private String remark;

    @Schema(title = "预计送达时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;

    @Schema(title = "配送状态", description = "1立即送出 0选择具体时间")
    private Integer deliveryStatus;

    @Schema(title = "餐具数量")
    private Integer tablewareNumber;

    @Schema(title = "餐具数量状态", description = "1按餐量提供  0选择具体数量")
    private Integer tablewareStatus;

    @Schema(title = "打包费")
    private Integer packAmount;

    @Schema(title = "总金额")
    private BigDecimal amount;
}
