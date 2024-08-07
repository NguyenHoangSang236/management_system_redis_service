package com.management_system.redis_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.redis_service.services.RedisService;
import com.management_system.utilities.constant.enumuration.FilterType;
import com.management_system.utilities.core.redis.RedisRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/unauthen/action", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class RedisController {
    @Autowired
    RedisService redisService;


    @PostMapping("/save")
    public ApiResponse saveData(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        RedisRequest request = objectMapper.readValue(json, RedisRequest.class);
        FilterType type = request.getType();
        Map<String, Object> data = request.getData();

        if (data == null) {
            return ApiResponse.builder()
                    .result("failed")
                    .message("Request does not have 'data' field")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else if (data.get("id") == null) {
            return ApiResponse.builder()
                    .result("failed")
                    .message("Data does not have 'id' field")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else if (type == null) {
            return ApiResponse.builder()
                    .result("failed")
                    .message("Request does not have 'type' field")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String key = type.name() + ":" + data.get("id");

        redisService.save(key, data);

        return ApiResponse.builder()
                .result("success")
                .content("Save data to Redis successfully")
                .status(HttpStatus.OK)
                .build();
    }


    @GetMapping("/findByKey-{key}")
    public ApiResponse findByKey(@PathVariable(value = "key") String key) throws JsonProcessingException {
        Map<String, Object> result = redisService.findByKey(key);

        if (result == null || result.keySet().isEmpty()) {
            return ApiResponse.builder()
                    .result("failed")
                    .message("No data!")
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result("success")
                    .message("Get data by key successfully")
                    .content(result)
                    .status(HttpStatus.OK)
                    .build();
        }
    }


    @GetMapping("/deleteByKey-{key}")
    public ApiResponse deleteByKey(@PathVariable(value = "key") String key) throws JsonProcessingException {
        redisService.delete(key);

        return ApiResponse.builder()
                .result("success")
                .message("Delete successfully")
                .status(HttpStatus.OK)
                .build();
    }


    @GetMapping("/cleanDatabase")
    public ApiResponse cleanDatabase() throws JsonProcessingException {
        redisService.cleanDatabase();

        return ApiResponse.builder()
                .result("success")
                .message("Clean database successfully")
                .status(HttpStatus.OK)
                .build();
    }
}
