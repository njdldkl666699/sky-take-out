package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "套餐分页查询DTO")
public class SetmealPageQueryDTO implements Serializable {

    @Schema(title = "页码")
    private int page;

    @Schema(title = "每页记录数")
    private int pageSize;

    @Schema(title = "套餐名称")
    private String name;

    @Schema(title = "分类id")
    private Integer categoryId;

    @Schema(title = "状态", description = "0表示禁用 1表示启用")
    private Integer status;

}
