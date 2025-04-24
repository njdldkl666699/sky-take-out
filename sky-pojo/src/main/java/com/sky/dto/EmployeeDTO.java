package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name="员工DTO",description = "员工数据传输对象")
public class EmployeeDTO implements Serializable {

    /**
     * 员工ID，前端可能不会传递
     */
    @Schema(name = "员工ID")
    private Long id;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "姓名")
    private String name;

    @Schema(name = "电话号码")
    private String phone;

    @Schema(name = "性别")
    private String sex;

    @Schema(name = "身份证号码")
    private String idNumber;

}
