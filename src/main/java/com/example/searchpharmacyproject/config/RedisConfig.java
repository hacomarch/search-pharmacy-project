package com.example.searchpharmacyproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//Redis 설정
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost; //localhost

    @Value("${spring.redis.port}")
    private int redisPort; //6379

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort); //Redis 호스트와 포트를 기반으로 연결 설정
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>(); //Redis에 데이터를 저장하고 검색하는 데 사용되는 클래스
        redisTemplate.setConnectionFactory(redisConnectionFactory()); //redisConnectionFactory()로부터 생성된 연결을 설정
        //문자열 키, 값에 대한 직렬화를 수행해서 Redis 키, 해시 키, 해시 값의 직렬화 방법을 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
