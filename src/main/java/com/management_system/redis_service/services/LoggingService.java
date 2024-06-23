package com.management_system.redis_service.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.redis_service.constant.ActionType;
import com.management_system.utilities.core.redis.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class LoggingService {
    public void log(ActionType type,
                    String key,
                    String hashKey,
                    Map<String, Object> data,
                    String message) {
        StringBuilder builder = new StringBuilder();
        Date date = new Date();

        builder.append("\n------------START Redis LOGGING--------------\n");
        builder.append("Time: ").append(date).append("\n");
        builder.append("Type: ").append(type.name()).append("\n");
        builder.append("Key: ").append(key).append("\n");
        builder.append("Hash key: ").append(hashKey).append("\n");
        builder.append("Hash data: ").append(data.toString()).append("\n");
        builder.append("Message: ").append(message);
        builder.append("\n------------END Redis LOGGING----------------\n");

        log.info(builder.toString());
    }
}
