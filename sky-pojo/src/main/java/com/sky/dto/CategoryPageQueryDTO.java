package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "分类分页查询DTO")
public class CategoryPageQueryDTO implements Serializable {

    //页码
    @Schema(title = "页码")
    private int page;

    @Schema(title = "每页记录数")
    private int pageSize;

    @Schema(title = "分类名称")
    private String name;

    @Schema(title = "分类类型", description = "1菜品分类  2套餐分类")
    private Integer type;

}
