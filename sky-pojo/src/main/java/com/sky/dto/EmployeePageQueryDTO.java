package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "员工分页查询", description = "员工分页查询传输对象")
public class EmployeePageQueryDTO implements Serializable {

    @Schema(title = "姓名")
    private String name;

    @Schema(title = "页码")
    private int page;

    @Schema(title = "每页显示记录数")
    private int pageSize;

}
