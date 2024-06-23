package com.management_system.redis_service.config;

import com.management_system.redis_service.constant.RedisConstantValue;
import com.management_system.utilities.core.redis.RedisData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
public class RedisConfig {
    // Redis client standalone connection
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(RedisConstantValue.REDIS_HOST_NAME, RedisConstantValue.REDIS_PORT)
        );
    }


    // Create a RedisTemplate to interact with Redis
    // - Key is Object, serialized as String
    // - Value is Object, serialized as JSON
    // - Hash key is serialized as String
    // - Hash value is serialized as String
    @Bean
    @Primary
    public RedisTemplate<String, Map<String, Object>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Map<String, Object>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }


    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Map<String, Object>> redisTemplate) {
        return redisTemplate.opsForHash();
    }
}
