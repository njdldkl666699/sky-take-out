package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "员工DTO", description = "员工数据传输对象")
public class EmployeeDTO implements Serializable {

    /**
     * 员工ID，前端可能不会传递
     */
    @Schema(title = "员工ID")
    private Long id;

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "姓名")
    private String name;

    @Schema(title = "电话号码")
    private String phone;

    @Schema(title = "性别")
    private String sex;

    @Schema(title = "身份证号码")
    private String idNumber;

}
