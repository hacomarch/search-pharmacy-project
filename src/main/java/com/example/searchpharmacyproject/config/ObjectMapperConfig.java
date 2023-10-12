package com.example.searchpharmacyproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//java 객체 -> json 데이터 : 직렬화
//json 데이터 -> java 객체 : 역직렬화
//웹 서비스에서 데이터를 주고 받을 대 유용하다.
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
