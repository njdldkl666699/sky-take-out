package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "订单搜索DTO")
public class OrdersPageQueryDTO implements Serializable {

    @Schema(title = "页码")
    private int page;

    @Schema(title = "每页记录数")
    private int pageSize;

    @Schema(title = "订单号")
    private String number;

    @Schema(title = "手机号")
    private String phone;

    @Schema(title = "订单状态")
    private Integer status;

    @Schema(title = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    @Schema(title = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(title = "用户ID")
    private Long userId;

}
