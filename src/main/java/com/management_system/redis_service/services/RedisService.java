package com.management_system.redis_service.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.redis_service.constant.ActionType;
import com.management_system.redis_service.core.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class RedisService {
    @Autowired
    LoggingService loggingService;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    HashOperations<Object, String, RedisData> hashOperations;


    @Autowired
    public RedisService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }


    public <T extends RedisData> void save(T data, String hashKey) {
        try {
            hashOperations.put(hashKey, data.getId(), data);
            loggingService.log(ActionType.SAVE, hashKey, null, data, null, "Saved data to Redis successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            loggingService.log(ActionType.SAVE, hashKey, null, data, null, e.getMessage());
        }
    }


    public Map<String, RedisData> findAll(String hashKey) {
        try {
            Map<String, RedisData> resultMap = hashOperations.entries(hashKey);
            loggingService.log(ActionType.SEARCH, hashKey, null, null, resultMap, null);

            return resultMap;
        }
        catch (Exception e) {
            e.printStackTrace();
            loggingService.log(ActionType.SAVE, hashKey, null, null, null, e.getMessage());

            return null;
        }
    }


    public <T extends RedisData> T findById(String id, String hashKey, Class<T> classType) {
        try {
            RedisData data = hashOperations.get(hashKey, id);

            if(data == null) {
                return null;
            }
            else {
                ObjectMapper objectMapper = new ObjectMapper();
                T resultData = objectMapper.convertValue(data, classType);
                Map<String, RedisData> resMap = objectMapper.convertValue(resultData, new TypeReference<Map<String, RedisData>>() {});

                loggingService.log(ActionType.SEARCH, hashKey, id, null, resMap, null);

                return resultData;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            loggingService.log(ActionType.SEARCH, hashKey, id, null, null, e.getMessage());

            return null;
        }
    }


    public void delete(String id, String hashKey) {
        hashOperations.delete(hashKey, id);
        loggingService.log(ActionType.DELETE, hashKey, id, null, null, "Deleted " + id + " of hash key " + hashKey);
    }


    public void deleteAll() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().commands().flushDb();
        loggingService.log(ActionType.DELETE, null, null, null, null, "Deleted all data");
    }
}
