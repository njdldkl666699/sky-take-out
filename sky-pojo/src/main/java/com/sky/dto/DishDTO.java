package com.sky.dto;

import com.sky.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(name="菜品DTO")
public class DishDTO implements Serializable {

    @Schema(name="菜品ID")
    private Long id;

    @Schema(name="菜品名称")
    private String name;

    @Schema(name="分类ID")
    private Long categoryId;

    @Schema(name="菜品价格")
    private BigDecimal price;

    @Schema(name="图片")
    private String image;

    @Schema(name="描述信息")
    private String description;

    @Schema(name="菜品状态", description = "0 停售 1 起售")
    private Integer status;

    @Schema(name="口味")
    private List<DishFlavor> flavors = new ArrayList<>();

}
