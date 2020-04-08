package com.littlefox.example.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2 {

    private boolean disable;

    @Bean
    public Docket createRestApi() {
        Predicate selector = PathSelectors.any();
        if (disable) {
            selector = PathSelectors.none();
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.example.controller"))
                .paths(selector)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(" 接口")
                .version("1.0.0")
                .contact(new Contact("rockychen","https://rockychen1221.github.io","rockychen1221@163.com"))
                .build();
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

}
