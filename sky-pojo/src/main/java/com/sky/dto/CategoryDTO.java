package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/*
TODO: 修改Schema注解的使用，将name改为title
 */

@Data
@Schema(name = "分类DTO")
public class CategoryDTO implements Serializable {

    @Schema(name = "分类ID", description = "主键")
    private Long id;

    @Schema(name = "分类类型", description = "1 菜品分类 2 套餐分类")
    private Integer type;

    @Schema(name = "分类名称")
    private String name;

    @Schema(name = "排序", description = "排序字段")
    private Integer sort;

}
