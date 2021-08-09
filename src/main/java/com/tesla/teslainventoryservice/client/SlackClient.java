package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.SlackPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class SlackClient {

    private final RestTemplate restTemplate;

    private final URI slackNotificationUrl;


    public SlackClient(final RestTemplate restTemplate, @Value("${tesla.notification-url}") final URI slackNotificationUrl) {
        this.restTemplate = restTemplate;
        this.slackNotificationUrl = slackNotificationUrl;
    }

    public void sendSlackNotification(final SlackPost slackPost) {
        restTemplate.postForEntity(slackNotificationUrl, slackPost, Void.class);
    }
}
