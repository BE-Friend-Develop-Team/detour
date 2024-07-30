package com.befriend.detour.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.auth.host}")
    private String host;
    @Value("${spring.data.redis.auth.port}")
    private int port; // 인증 서버
    @Value("${spring.data.redis.auth.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisAuthConnectionFactory() {
        // 레디스와의 서버 연결 설정 (레디스의 서버 주소, 포트)
        final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword((password));

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "redisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        // stringRedisTemplate 에서 setConnectionFactory 라는 메서드로 서버와 연결 될 수 있도록 구현
        stringRedisTemplate.setConnectionFactory(redisAuthConnectionFactory());

        return stringRedisTemplate;
    }

}