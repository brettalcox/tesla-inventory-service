package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.DiscordPost;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class DiscordClient {

    private final RestTemplate restTemplate;

    public DiscordClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttempts = 10, backoff = @Backoff(delay = 250, multiplier = 2))
    public void sendNotification(final DiscordPost discordPost, final URI notificationEndpoint) {
        restTemplate.postForEntity(notificationEndpoint, discordPost, Void.class);
    }
}
