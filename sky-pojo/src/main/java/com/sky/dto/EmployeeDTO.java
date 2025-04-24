package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "员工数据传输对象")
public class EmployeeDTO implements Serializable {

    /**
     * 员工ID，前端可能不会传递
     */
    @Schema(description = "员工ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "身份证号码")
    private String idNumber;

}
