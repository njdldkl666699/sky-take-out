package com.sky.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，注册Knife4j相关组件
 */
@Configuration
@Slf4j
public class Knife4jConfiguration {

    /**
     * 设置基本信息
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        log.info("开始注册Knife4j...");
        return new OpenAPI()
                .info(new Info()
                        .title("苍穹外卖项目接口文档")
                        .version("2.0")
                        .description("苍穹外卖项目接口文档"));
    }
}
