package com.befriend.detour.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

   /* @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;
*/
    @Value("${spring.data.redis.auth.host}")
    private String redisHost;
    @Value("${spring.data.redis.auth.port}")
    private int redisPort; // 인증 서버
    @Value("${spring.data.redis.auth.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(password);

        return Redisson.create(config);
    }

}
