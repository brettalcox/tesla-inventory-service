package com.tesla.teslainventoryservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URI;
import java.util.Map;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "tesla")
@EnableScheduling
@EnableRetry
public class TeslaInventoryScheduleConfig {
    private Map<String, Map<String, URI>> notificationEndpoints;

    public Map<String, Map<String, URI>> getNotificationEndpoints() {
        return notificationEndpoints;
    }

    public void setNotificationEndpoints(Map<String, Map<String, URI>> notificationEndpoints) {
        this.notificationEndpoints = notificationEndpoints;
    }
}
