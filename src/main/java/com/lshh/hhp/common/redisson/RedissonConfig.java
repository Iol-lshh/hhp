package com.lshh.hhp.common.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String SPRING_DATA_REDIS_HOST;

    @Value("${spring.data.redis.port}")
    private int SPRING_DATA_REDIS_PORT;
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(
                String.format("redis://%s:%d", SPRING_DATA_REDIS_HOST, SPRING_DATA_REDIS_PORT ));
        return Redisson.create(config);
    }
}
