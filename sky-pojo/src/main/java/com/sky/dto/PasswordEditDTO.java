package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "修改密码DTO")
public class PasswordEditDTO implements Serializable {

    @Schema(title = "员工id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long empId;

    @Schema(title = "旧密码")
    private String oldPassword;

    @Schema(title = "新密码")
    private String newPassword;

}
