package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
@Schema(title = "用户登录DTO")
public class UserLoginDTO implements Serializable {

    @Schema(title = "微信授权码")
    private String code;

}
