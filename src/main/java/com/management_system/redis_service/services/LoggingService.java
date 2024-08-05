package com.management_system.redis_service.services;

import com.management_system.redis_service.constant.ActionType;
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
        Date date = new Date();

        String builder = "\n------------START Redis LOGGING--------------\n" +
                "Time: " + date + "\n" +
                "Type: " + type.name() + "\n" +
                "Key: " + key + "\n" +
                "Hash key: " + hashKey + "\n" +
                "Hash data: " + data.toString() + "\n" +
                "Message: " + message +
                "\n------------END Redis LOGGING----------------\n";

        log.info(builder);
    }
}
