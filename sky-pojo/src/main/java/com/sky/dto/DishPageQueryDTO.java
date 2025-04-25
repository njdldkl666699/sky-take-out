package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "菜品分页查询DTO")
public class DishPageQueryDTO implements Serializable {

    @Schema(name = "页码")
    private int page;

    @Schema(name = "每页记录数")
    private int pageSize;

    @Schema(name = "菜品名称")
    private String name;

    @Schema(name = "分类id")
    private Integer categoryId;

    @Schema(name = "状态", description = "0表示禁用 1表示启用")
    private Integer status;

}
