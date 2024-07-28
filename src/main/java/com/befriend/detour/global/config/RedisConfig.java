package com.befriend.detour.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.auth.host}")
    private String host;
    @Value("${spring.data.redis.auth.port}")
    private int port; // 인증 서버

    @Bean
    public RedisConnectionFactory redisAuthConnectionFactory() {

        // 레디스와의 서버 연결 설정 (레디스의 서버 주소, 포트)
        return new LettuceConnectionFactory(host, port);
    }

    // 빈 이름을 redisTemplate 으로 설정해주지 않으면
    // A component required a bean named 'redisTemplate' that could not be found. 오류 발생
    @Bean(name = "redisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        // stringRedisTemplate 에서 setConnectionFactory 라는 메서드로 서버와 연결 될 수 있도록 구현
        stringRedisTemplate.setConnectionFactory(redisAuthConnectionFactory());

        return stringRedisTemplate;
    }

}