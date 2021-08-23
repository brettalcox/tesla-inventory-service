package com.tesla.teslainventoryservice.config;

import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TeslaInventoryConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TaskSchedulerCustomizer taskSchedulerCustomizer() {
        return threadPoolTaskScheduler -> threadPoolTaskScheduler.setPoolSize(5);
    }
}
