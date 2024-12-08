package com.management_system.redis_service;

import com.management_system.utilities.utils.InitiateConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class},
        scanBasePackages = {
                "com.management_system.redis_service.config",
                "com.management_system.redis_service.constant",
                "com.management_system.redis_service.controller",
                "com.management_system.redis_service.services",
                "com.management_system.utilities",
        }
)
@ComponentScan(basePackages = {
        "com.management_system.redis_service.config",
        "com.management_system.redis_service.constant",
        "com.management_system.redis_service.controller",
        "com.management_system.redis_service.services",
        "com.management_system.utilities",
})
//@EnableDiscoveryClient
public class RedisServiceApplication {
    @Autowired
    InitiateConfigUtils initiateConfigUtils;

    public static void main(String[] args) {
        SpringApplication.run(RedisServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initSslConfig() {
        initiateConfigUtils.initSslConfig();
    }
}
