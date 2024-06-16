package com.management_system.redis_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.redis_service.core.RedisData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisService {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    HashOperations<Object, String, RedisData> hashOperations;


    @Autowired
    public RedisService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }


    public <T extends RedisData> void save(T data, String hashKey) {
        hashOperations.put(hashKey, data.getId(), data);
    }


    public Map<String, RedisData> findAll(String hashKey) {
        return hashOperations.entries(hashKey);
    }


    public <T extends RedisData> T findById(String id, String hashKey, Class<T> classType) {
        RedisData data = hashOperations.get(hashKey, id);

        if(data == null) {
            return null;
        }
        else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(data, classType);
        }
    }


    public void delete(String id, String hashKey) {
        hashOperations.delete(hashKey, id);
    }
}
