package com.sky.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "菜品口味")
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "菜品口味id")
    private Long id;

    @Schema(title = "菜品id")
    private Long dishId;

    @Schema(title = "口味名称")
    private String name;

    @Schema(title = "口味数据列表")
    private String value;

}
