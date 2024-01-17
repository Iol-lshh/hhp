package com.lshh.hhp.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    @DisplayName("test 레디스 연결 확인")
    public void testRedisConnection() {
        assertNotNull(redissonClient);
        assertNotNull(redissonClient.getConfig());
    }
}