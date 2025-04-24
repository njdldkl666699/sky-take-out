package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name="员工分页查询",description = "员工分页查询传输对象")
public class EmployeePageQueryDTO implements Serializable {

    @Schema(name = "姓名")
    private String name;

    @Schema(name = "页码")
    private int page;

    @Schema(name = "每页显示记录数")
    private int pageSize;

}
