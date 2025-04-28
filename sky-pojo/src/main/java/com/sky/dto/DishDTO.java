package com.sky.dto;

import com.sky.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(title = "菜品DTO")
public class DishDTO implements Serializable {

    @Schema(title = "菜品ID")
    private Long id;

    @Schema(title = "菜品名称")
    private String name;

    @Schema(title = "分类ID")
    private Long categoryId;

    @Schema(title = "菜品价格")
    private BigDecimal price;

    @Schema(title = "图片")
    private String image;

    @Schema(title = "描述信息")
    private String description;

    @Schema(title = "菜品状态", description = "0 停售 1 起售")
    private Integer status;

    @Schema(title = "口味")
    private List<DishFlavor> flavors = new ArrayList<>();

}
