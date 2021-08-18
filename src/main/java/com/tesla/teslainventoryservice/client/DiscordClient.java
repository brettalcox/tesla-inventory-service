package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.DiscordPost;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class DiscordClient {

    private final RestTemplate restTemplate;

    public DiscordClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(final DiscordPost discordPost, final URI notificationEndpoint) {
        restTemplate.postForEntity(notificationEndpoint, discordPost, Void.class);
    }
}
