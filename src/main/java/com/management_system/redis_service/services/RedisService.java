package com.management_system.redis_service.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    @Autowired
    LoggingService loggingService;

    @Autowired
    RedisTemplate<String, Map<String, Object>> redisTemplate;

    HashOperations<String, String, Object> hashOperations;


    @Autowired
    public RedisService(RedisTemplate<String, Map<String, Object>> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }


    public void save(String key, Map<String, Object> hashData) {
        try {
            hashOperations.putAll(key, hashData);
            redisTemplate.expire(key, 12, TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map<String, Object> findByKey(String key) {
        try {
            Map<String, Object> resultMap = hashOperations.entries(key);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    public void delete(String key) {
        redisTemplate.delete(key);
    }


    public void cleanDatabase() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().commands().flushDb();
    }


    @PostConstruct
    public void init() {
        cleanDatabase();
        log.info("Redis database cleaned at startup.");
    }
}
