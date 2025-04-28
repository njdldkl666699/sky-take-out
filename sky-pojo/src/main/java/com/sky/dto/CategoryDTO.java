package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "分类DTO")
public class CategoryDTO implements Serializable {

    @Schema(title = "分类ID", description = "主键")
    private Long id;

    @Schema(title = "分类类型", description = "1 菜品分类 2 套餐分类")
    private Integer type;

    @Schema(title = "分类名称")
    private String name;

    @Schema(title = "排序", description = "排序字段")
    private Integer sort;

}
