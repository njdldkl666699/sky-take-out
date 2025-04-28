package com.sky.dto;

import com.sky.entity.SetmealDish;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(title = "套餐DTO")
public class SetmealDTO implements Serializable {

    @Schema(title = "套餐ID")
    private Long id;

    @Schema(title = "分类ID")
    private Long categoryId;

    @Schema(title = "套餐名称")
    private String name;

    @Schema(title = "套餐价格")
    private BigDecimal price;

    @Schema(title = "套餐状态", description = "0:停用 1:启用")
    private Integer status;

    @Schema(title = "描述信息")
    private String description;

    @Schema(title = "图片")
    private String image;

    @Schema(title = "套餐菜品关系")
    private List<SetmealDish> setmealDishes = new ArrayList<>();

}
