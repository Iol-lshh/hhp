package com.lshh.hhp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestRedissonConfig {
    private static final String REDIS_CONTAINER_NAME = "redis:latest";

    private static final GenericContainer<?> redisContainer;

    static {
        redisContainer = new GenericContainer<>(
                DockerImageName
                    .parse(REDIS_CONTAINER_NAME)
                    .asCompatibleSubstituteFor("redis:7.2.4"))
                .withExposedPorts(6379);
        redisContainer.start();
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(
                String.format("redis://%s:%d", redisContainer.getHost(), redisContainer.getFirstMappedPort()));
        return Redisson.create(config);
    }
}