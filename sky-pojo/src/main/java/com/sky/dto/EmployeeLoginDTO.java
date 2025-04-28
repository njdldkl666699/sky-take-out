package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "员工登录DTO", description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO implements Serializable {

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "密码")
    private String password;

}
