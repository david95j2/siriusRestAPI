package com.sierrabase.siriusapi.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Getter
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://211.224.129.230:40000", // 출장용
                        "http://211.224.129.230:50000", // GS
                        "http://211.224.129.230:60000", // 개발용
                        "http://192.168.0.160:8080", // 인혁이형 로컬
                        "http://192.168.10.6:8080", // 신이사님 로컬
                        "http://192.168.0.76:20010") // 윈도우 로컬
                .allowedMethods("*");
    }
}
