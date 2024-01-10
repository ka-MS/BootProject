package com.example.bootproject.common;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /*
    * Docket -> OpenAPI 로 변경
    * @ApiResponse 의 일부 설정과 @ExampleObject 설정이 적용 되지 않았음
    * Springfox가 OpenAPI 2.0(Swagger 2) 사양을 기반 으로 하기 때문에 기능이 제한적 일 수 있다 하여
    * springfox-swagger-ui 에서 springdoc-openapi-ui로 변경
     */
    @Bean
    public OpenAPI api(){
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
                .title("BootProject Swagger")
                .version(("beta"))
                .description("BootProject Rest API Documentation");
    }
}
