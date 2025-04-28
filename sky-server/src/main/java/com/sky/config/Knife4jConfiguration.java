package com.sky.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
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
     *
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

    /**
     * 设置管理端接口文档
     *
     * @return
     */
    @Bean
    public GroupedOpenApi adminGroupOpenAPI() {
        log.info("开始注册管理端接口文档...");
        return GroupedOpenApi.builder()
                .group("admin")
                .displayName("管理端接口")
                .pathsToMatch("/admin/**")
                .packagesToScan("com.sky.controller.admin")
                .build();
    }

    /**
     * 设置用户端接口文档
     *
     * @return
     */
    @Bean
    public GroupedOpenApi userGroupOpenAPI() {
        log.info("开始注册用户端接口文档...");
        return GroupedOpenApi.builder()
                .group("user")
                .displayName("用户端接口")
                .pathsToMatch("/user/**")
                .packagesToScan("com.sky.controller.user")
                .build();
    }
}
