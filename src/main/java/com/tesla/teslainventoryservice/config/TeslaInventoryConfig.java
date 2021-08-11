package com.tesla.teslainventoryservice.config;

import com.tesla.teslainventoryservice.client.SlackClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Configuration
public class TeslaInventoryConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SlackClient inventoryNotificationSlackClient(@Value("${tesla.notification-url}") final URI url, final RestTemplate restTemplate) {
        return new SlackClient(restTemplate, url);
    }

    @Bean
    public SlackClient errorSlackClient(@Value("${tesla.error-notification-url}") final URI url, final RestTemplate restTemplate) {
        return new SlackClient(restTemplate, url);
    }
}
