package com.management_system.redis_service.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.redis_service.constant.ActionType;
import com.management_system.redis_service.core.RedisData;

import java.util.Date;
import java.util.Map;

public class LoggingService {
    public <T extends RedisData> void log(ActionType type,
                                                      String hashKey,
                                                      String id,
                                                      T data,
                                                      Map<String, Object> resultMap, String message) {
        StringBuilder builder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        Date date = new Date();
        Map<String, Object> dataMap = objectMapper.convertValue(data, new TypeReference<Map<String, Object>>() {});

        builder.append("\n------------START Redis LOGGING--------------\n");
        builder.append("Time: ").append(date).append("\n");
        builder.append("Type: ").append(type.name()).append("\n");
        builder.append("Hash key: ").append(hashKey).append("\n");
        builder.append("Data ID: ").append(id).append("\n");
        builder.append("Data: ").append(dataMap.toString()).append("\n");
        builder.append("Result: ").append(resultMap != null ? resultMap.toString() : message).append("\n");


        builder.append("\n------------END Redis LOGGING----------------\n");
    }
}
